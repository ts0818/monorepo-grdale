// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.example.demo.service.azure.media;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.management.exception.ManagementException;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.mediaservices.MediaServicesManager;
import com.azure.resourcemanager.mediaservices.models.Asset;
import com.azure.resourcemanager.mediaservices.models.AssetContainerPermission;
import com.azure.resourcemanager.mediaservices.models.AssetContainerSas;
import com.azure.resourcemanager.mediaservices.models.BuiltInStandardEncoderPreset;
import com.azure.resourcemanager.mediaservices.models.EncoderNamedPreset;
import com.azure.resourcemanager.mediaservices.models.Job;
import com.azure.resourcemanager.mediaservices.models.JobInput;
import com.azure.resourcemanager.mediaservices.models.JobInputAsset;
import com.azure.resourcemanager.mediaservices.models.JobOutput;
import com.azure.resourcemanager.mediaservices.models.JobOutputAsset;
import com.azure.resourcemanager.mediaservices.models.JobState;
import com.azure.resourcemanager.mediaservices.models.ListContainerSasInput;
import com.azure.resourcemanager.mediaservices.models.ListPathsResponse;
import com.azure.resourcemanager.mediaservices.models.StreamingEndpoint;
import com.azure.resourcemanager.mediaservices.models.StreamingEndpointResourceState;
import com.azure.resourcemanager.mediaservices.models.StreamingLocator;
import com.azure.resourcemanager.mediaservices.models.StreamingPath;
import com.azure.resourcemanager.mediaservices.models.Transform;
import com.azure.resourcemanager.mediaservices.models.TransformOutput;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

@Service
public class EncodingWithMESPredefinedPreset {
	
	@Autowired
	private ConfigWrapper configWrapper;
	
    private static final String TRANSFORM_NAME = "AdaptiveBitrate";
    private static final String OUTPUT_FOLDER = "Output";
    //private static final String BASE_URI = "https://nimbuscdn-nimbuspm.streaming.mediaservices.windows.net/2b533311-b215-4409-80af-529c3e853622/";
    //private static final String INPUT_LABEL = "input1";

    // Please change this to your endpoint name
    private static final String STREAMING_ENDPOINT_NAME = "default";

    @Value("${azure.key.vault.url}")
    private String azureKeyVaultUrl;
    
    @Value("${azure.media.managed.id}")
    private String azureMediaManagedId;
    
    private MediaServicesManager createMediaServiceManagerByKeyVault () {
        DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder()
                .managedIdentityClientId(azureMediaManagedId)
                .build();

            // Azure SDK client builders accept the credential as a parameter
            SecretClient client = new SecretClientBuilder()
                .vaultUrl(azureKeyVaultUrl)
                .credential(defaultCredential)
                .buildClient();
            
            AzureProfile profile = new AzureProfile(configWrapper.getAadTenantId(), configWrapper.getAzureSubscriptionId(),
                    com.azure.core.management.AzureEnvironment.AZURE);

            MediaServicesManager manager = MediaServicesManager.configure()
                    .withLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
                    .authenticate(defaultCredential, profile);
            return manager; 
    }
    
//    private MediaServicesManager createMediaServicesManager () {
//        // Connect to media services, please see https://docs.microsoft.com/en-us/azure/media-services/latest/configure-connect-java-howto
//        // for details.
//        TokenCredential credential = new ClientSecretCredentialBuilder()
//                .clientId(configWrapper.getAzureClientId())
//                .clientSecret(configWrapper.getAzureClientSecret())
//                .tenantId(configWrapper.getAadTenantId())
//                .build();
//        AzureProfile profile = new AzureProfile(configWrapper.getAadTenantId(), configWrapper.getAzureSubscriptionId(),
//                com.azure.core.management.AzureEnvironment.AZURE);
//
//        // MediaServiceManager is the entry point to Azure Media resource management.
//        MediaServicesManager manager = MediaServicesManager.configure()
//                .withLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
//                .authenticate(credential, profile);
//        return manager;
//    }

    /**
     * Run the sample.
     *
     * @param config This param is of type ConfigWrapper. This class reads values from local configuration file.
     */
    public String runEncodingWithMESPredefinedPreset(MultipartFile uploadFile) {
        // MediaServiceManager is the entry point to Azure Media resource management.
        //MediaServicesManager manager = createMediaServicesManager();
        MediaServicesManager manager = createMediaServiceManagerByKeyVault();
        // Creating a unique suffix so that we don't have name collisions if you run the
        // sample
        UUID uuid = UUID.randomUUID();
        String uniqueness = uuid.toString();
        String jobName = "job-" + uniqueness.substring(0, 13);
        String locatorName = "locator-" + uniqueness;
        String outputAssetName = "output-" + uniqueness;
        String inputAssetName = "input-" + uniqueness;
        boolean stopEndpoint = false;

        String streamingUrl = null;
        try {
            List<TransformOutput> outputs = new ArrayList<>();
            outputs.add(new TransformOutput().withPreset(
                    new BuiltInStandardEncoderPreset().withPresetName(EncoderNamedPreset.CONTENT_AWARE_ENCODING)));

            // Create the transform.
            System.out.println("Creating a transform...");
            Transform transform = manager.transforms()
                    .define(TRANSFORM_NAME)
                    .withExistingMediaService(configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName())
                    .withOutputs(outputs)
                    .create();
            System.out.println("Transform created");

            // Create a JobInputHttp. The input to the Job is a HTTPS URL pointing to an MP4 file.
//            List<String> files = new ArrayList<>();
//            files.add(uploadFile.getOriginalFilename());
//            JobInputHttp input = new JobInputHttp().withBaseUri(BASE_URI);
//            input.withFiles(files);
//            input.withLabel(INPUT_LABEL);

            Asset inputAsset = createInputAsset(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), inputAssetName,
            		uploadFile);
            
            // Output from the encoding Job must be written to an Asset, so let's create one. Note that we
            // are using a unique asset name, there should not be a name collision.
            System.out.println("Creating an output asset...");
            Asset outputAsset = manager.assets()
                    .define(outputAssetName)
                    .withExistingMediaService(configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName())
                    .create();

            Job job = submitJob(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), transform.name(), jobName,
            		inputAsset.name(), outputAsset.name());

            long startedTime = System.currentTimeMillis();

            // In this demo code, we will poll for Job status. Polling is not a recommended best practice for production
            // applications because of the latency it introduces. Overuse of this API may trigger throttling. Developers
            // should instead use Event Grid. To see how to implement the event grid, see the sample
            // https://github.com/Azure-Samples/media-services-v3-java/tree/master/ContentProtection/BasicAESClearKey.
            job = waitForJobToFinish(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), transform.name(),
                    jobName);

            long elapsed = (System.currentTimeMillis() - startedTime) / 1000; // Elapsed time in seconds
            System.out.println("Job elapsed time: " + elapsed + " second(s).");            


            if (job.state() == JobState.FINISHED) {
                System.out.println("Job finished.");
                System.out.println();

                // Now that the content has been encoded, publish it for Streaming by creating
                // a StreamingLocator. 
                StreamingLocator locator = getStreamingLocator(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(),
                        outputAsset.name(), locatorName);

                StreamingEndpoint streamingEndpoint = manager.streamingEndpoints()
                        .get(configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), STREAMING_ENDPOINT_NAME);

                if (streamingEndpoint != null) {
                    // Start The Streaming Endpoint if it is not running.
                    if (streamingEndpoint.resourceState() != StreamingEndpointResourceState.RUNNING) {
                        System.out.println("Streaming endpoint was stopped, restarting it...");
                        manager.streamingEndpoints().start(configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), STREAMING_ENDPOINT_NAME);

                        // We started the endpoint, we should stop it in cleanup.
                        stopEndpoint = true;
                    }

                    System.out.println();
                    System.out.println("Streaming urls:");
                    List<String> urls = getStreamingUrls(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), locator.name(), streamingEndpoint);

                    for (String url : urls) {
                        System.out.println("\t" + url);                        
                    }
                    streamingUrl = urls.get(0);

                    System.out.println();
                    System.out.println("To stream, copy and paste the Streaming URL into the Azure Media Player at 'http://aka.ms/azuremediaplayer'.");
                    System.out.println("When finished, press ENTER to continue.");
                    System.out.println();
                    System.out.flush();
                    

                    // Download output asset for verification.
                    System.out.println("Downloading output asset...");
                    System.out.println();
                    File outputFolder = new File(OUTPUT_FOLDER);
                    if (outputFolder.exists() && !outputFolder.isDirectory()) {
                        outputFolder = new File(OUTPUT_FOLDER + uniqueness);
                    }
                    if (!outputFolder.exists()) {
                        outputFolder.mkdir();
                    }

                    downloadResults(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), outputAsset.name(),
                            outputFolder);
                    
                    uploadResults(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), outputAsset.name(),
                            outputFolder);

                    System.out.println("Done downloading. Please check the files at " + outputFolder.getAbsolutePath());
                } else {
                    System.out.println("Could not find streaming endpoint: " + STREAMING_ENDPOINT_NAME);
                }

                System.out.println("When finished, press ENTER to cleanup.");
                System.out.println();
                System.out.flush();
               
            } else if (job.state() == JobState.ERROR) {
                System.out.println("ERROR: Job finished with error message: " + job.outputs().get(0).error().message());
                System.out.println("ERROR:                   error details: "
                        + job.outputs().get(0).error().details().get(0).message());
            }
        } catch (Exception e) {
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof AuthenticationException) {
                    System.out.println("ERROR: Authentication error, please check your account settings in appsettings.json.");
                    break;
                } else if (cause instanceof ManagementException) {
                    ManagementException apiException = (ManagementException) cause;
                    System.out.println("ERROR: " + apiException.getValue().getMessage());
                    break;
                }
                cause = cause.getCause();
            }
            System.out.println();
            e.printStackTrace();
            System.out.println();
        } finally {
            System.out.println("Cleaning up...");
            cleanup(manager, configWrapper.getAzureResourceGroup(), configWrapper.getAzureMediaServicesAccountName(), TRANSFORM_NAME, jobName,
                    outputAssetName, locatorName, stopEndpoint, STREAMING_ENDPOINT_NAME);
            System.out.println("Done.");
        }
        return streamingUrl;
    }

    /**
     * Create and submit a job.
     *
     * @param manager         The entry point of Azure Media resource management.
     * @param resourceGroup   The name of the resource group within the Azure subscription.
     * @param accountName     The Media Services account name.
     * @param transformName   The name of the transform.
     * @param jobName         The name of the job.
     * @param jobInput        The input to the job.
     * @param outputAssetName The name of the asset that the job writes to.
     * @return The job created.
     */
    private static Job submitJob(MediaServicesManager manager, String resourceGroup, String accountName, String transformName,
                                 String jobName, String inputAssetName,  String outputAssetName) {
        System.out.println("Creating a job...");
        // Use the name of the created input asset to create the job input.
        JobInput jobInput = new JobInputAsset().withAssetName(inputAssetName);

        // First specify where the output(s) of the Job need to be written to
        List<JobOutput> jobOutputs = new ArrayList<>();
        jobOutputs.add(new JobOutputAsset().withAssetName(outputAssetName));

        Job job = manager.jobs().define(jobName)
                .withExistingTransform(resourceGroup, accountName, transformName)
                .withInput(jobInput)
                .withOutputs(jobOutputs)
                .create();

        return job;
    }

    /**
     * Polls Media Services for the status of the Job.
     *
     * @param manager       This is the entry point of Azure Media resource
     *                      management
     * @param resourceGroup The name of the resource group within the Azure
     *                      subscription
     * @param accountName   The Media Services account name
     * @param transformName The name of the transform
     * @param jobName       The name of the job submitted
     * @return The job
     */
    private static Job waitForJobToFinish(MediaServicesManager manager, String resourceGroup, String accountName,
                                          String transformName, String jobName) {
        final int SLEEP_INTERVAL = 10 * 1000;

        Job job = null;
        boolean exit = false;

        do {
            job = manager.jobs().get(resourceGroup, accountName, transformName, jobName);

            if (job.state() == JobState.FINISHED || job.state() == JobState.ERROR || job.state() == JobState.CANCELED) {
                exit = true;
            } else {
                System.out.println("Job is " + job.state());

                int i = 0;
                for (JobOutput output : job.outputs()) {
                    System.out.print("\tJobOutput[" + i++ + "] is " + output.state() + ".");
                    if (output.state() == JobState.PROCESSING) {
                        System.out.print("  Progress: " + output.progress());
                    }
                    System.out.println();
                }

                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (!exit);

        return job;
    }

    /**
     * Use Media Service and Storage APIs to download the output files to a local folder
     *
     * @param manager       The entry point of Azure Media resource management
     * @param resourceGroup The name of the resource group within the Azure subscription
     * @param accountName   The Media Services account name
     * @param assetName     The asset name
     * @param outputFolder  The output folder for downloaded files.
     * @throws Exception
     * @throws URISyntaxException
     * @throws IOException
     */
    private static void downloadResults(MediaServicesManager manager, String resourceGroup, String accountName,
                                        String assetName, File outputFolder) throws URISyntaxException, IOException {
        ListContainerSasInput parameters = new ListContainerSasInput()
                .withPermissions(AssetContainerPermission.READ)
                .withExpiryTime(OffsetDateTime.now().plusHours(1));
        AssetContainerSas assetContainerSas = manager.assets()
                .listContainerSas(resourceGroup, accountName, assetName, parameters);

        BlobContainerClient container =
                new BlobContainerClientBuilder()
                        .endpoint(assetContainerSas.assetContainerSasUrls().get(0))
                        .buildClient();

        File directory = new File(outputFolder, assetName);
        directory.mkdir();

        container.listBlobs().forEach(blobItem -> {
            BlobClient blob = container.getBlobClient(blobItem.getName());
            File downloadTo = new File(directory, blobItem.getName());
            blob.downloadToFile(downloadTo.getAbsolutePath());
        });

        System.out.println("Download complete.");
    }

    /**
     * Creates a StreamingLocator for the specified asset and with the specified streaming policy name.
     * Once the StreamingLocator is created the output asset is available to clients for playback.
     *
     * @param manager       The entry point of Azure Media resource management
     * @param resourceGroup The name of the resource group within the Azure subscription
     * @param accountName   The Media Services account name
     * @param assetName     The name of the output asset
     * @param locatorName   The StreamingLocator name (unique in this case)
     * @return The locator created
     */
    private static StreamingLocator getStreamingLocator(MediaServicesManager manager, String resourceGroup, String accountName,
                                                        String assetName, String locatorName) {
        // Note that we are using one of the PredefinedStreamingPolicies which tell the Origin component
        // of Azure Media Services how to publish the content for streaming.
        System.out.println("Creating a streaming locator...");
        StreamingLocator locator = manager
                .streamingLocators().define(locatorName)
                .withExistingMediaService(resourceGroup, accountName)
                .withAssetName(assetName)
                .withStreamingPolicyName("Predefined_ClearStreamingOnly")
                .create();

        return locator;
    }

    /**
     * Checks if the streaming endpoint is in the running state, if not, starts it.
     *
     * @param manager           The entry point of Azure Media resource management
     * @param resourceGroup     The name of the resource group within the Azure subscription
     * @param accountName       The Media Services account name
     * @param locatorName       The name of the StreamingLocator that was created
     * @param streamingEndpoint The streaming endpoint.
     * @return List of streaming urls
     */
    private static List<String> getStreamingUrls(MediaServicesManager manager, String resourceGroup, String accountName,
                                                 String locatorName, StreamingEndpoint streamingEndpoint) {
        List<String> streamingUrls = new ArrayList<>();

        ListPathsResponse paths = manager.streamingLocators().listPaths(resourceGroup, accountName, locatorName);

        for (StreamingPath path : paths.streamingPaths()) {
            StringBuilder uriBuilder = new StringBuilder();
            uriBuilder.append("https://")
                    .append(streamingEndpoint.hostname())
                    //.append("/")
                    .append(path.paths().get(0));

            streamingUrls.add(uriBuilder.toString());
        }
        return streamingUrls;
    }

    /**
     * Cleanup
     *
     * @param manager               The entry point of Azure Media resource management.
     * @param resourceGroupName     The name of the resource group within the Azure subscription.
     * @param accountName           The Media Services account name.
     * @param transformName         The transform name.
     * @param jobName               The job name.
     * @param assetName             The asset name.
     * @param streamingLocatorName  The streaming locator name.
     * @param stopEndpoint          Stop endpoint if true, otherwise keep endpoint running.
     * @param streamingEndpointName The endpoint name.
     */
    private static void cleanup(MediaServicesManager manager, String resourceGroupName, String accountName, String transformName, String jobName,
                                String assetName, String streamingLocatorName, boolean stopEndpoint, String streamingEndpointName) {
        if (manager == null) {
            return;
        }

        manager.jobs().delete(resourceGroupName, accountName, transformName, jobName);
        //manager.assets().delete(resourceGroupName, accountName, assetName);

        //manager.streamingLocators().delete(resourceGroupName, accountName, streamingLocatorName);

        if (stopEndpoint) {
            // Because we started the endpoint, we'll stop it.
            manager.streamingEndpoints().stop(resourceGroupName, accountName, streamingEndpointName);
        } else {
            // We will keep the endpoint running because it was not started by this sample. Please note, There are costs to keep it running.
            // Please refer https://azure.microsoft.com/en-us/pricing/details/media-services/ for pricing.
            System.out.println("The endpoint '" + streamingEndpointName + "' is running. To halt further billing on the endpoint, please stop it in azure portal or AMS Explorer.");
        }
    }
    /**
     * Creates a new input Asset and uploads the specified local video file into it.
     *
     * @param manager           This is the entry point of Azure Media resource
     *                          management.
     * @param resourceGroupName The name of the resource group within the Azure
     *                          subscription.
     * @param accountName       The Media Services account name.
     * @param assetName         The name of the asset where the media file to
     *                          uploaded to.
     * @param mediaFile         The path of a media file to be uploaded into the
     *                          asset.
     * @return The asset.
     */
    private static Asset createInputAsset(MediaServicesManager manager, String resourceGroupName, String accountName,
            String assetName, MultipartFile mediaFile) throws Exception {

        System.out.println("Creating an input asset...");
        // Call Media Services API to create an Asset.
        // This method creates a container in storage for the Asset.
        // The files (blobs) associated with the asset will be stored in this container.
        Asset asset = manager.assets().define(assetName).withExistingMediaService(resourceGroupName, accountName)
                .create();

        // Use Media Services API to get back a response that contains
        // SAS URL for the Asset container into which to upload blobs.
        // That is where you would specify read-write permissions
        // and the expiration time for the SAS URL.
        ListContainerSasInput parameters = new ListContainerSasInput()
                .withPermissions(AssetContainerPermission.READ_WRITE).withExpiryTime(OffsetDateTime.now().plusHours(4));
        AssetContainerSas response = manager.assets()
                .listContainerSas(resourceGroupName, accountName, assetName, parameters);

        // Use Storage API to get a reference to the Asset container
        // that was created by calling Asset's create method.
        BlobContainerClient container = new BlobContainerClientBuilder()
                .endpoint(response.assetContainerSasUrls().get(0))
                .buildClient();

        // Uploading from a local file:
       // URI fileToUpload = StreamHLSAndDASH.class.getClassLoader().getResource(mediaFile).toURI(); // The file is a
        // resource in
        // CLASSPATH.
        //File file = new File(fileToUpload);
        BlobClient blob = container.getBlobClient(mediaFile.getOriginalFilename());

        // Use Storage API to upload the file into the container in storage.
        System.out.println("Uploading a media file to the asset...");
        blob.upload(mediaFile.getInputStream(), mediaFile.getBytes().length);
        //blob.uploadFromFile(mediaFile.getOriginalFilename());

        return asset;
    }
    
	private static void uploadResults(MediaServicesManager manager, String resourceGroup, String accountName,
			String assetName, File outputFolder) throws URISyntaxException, IOException {
		ListContainerSasInput parameters = new ListContainerSasInput().withPermissions(AssetContainerPermission.READ)
				.withExpiryTime(OffsetDateTime.now().plusHours(1));
		AssetContainerSas assetContainerSas = manager.assets().listContainerSas(resourceGroup, accountName, assetName,
				parameters);

		BlobContainerClient container = new BlobContainerClientBuilder()
				.endpoint(assetContainerSas.assetContainerSasUrls().get(0))
				.buildClient();

		container.listBlobs().forEach(blobItem -> {
			BlobClient blob = container.getBlobClient(blobItem.getName());
			//blob.copyFromUrl(blob.getBlobUrl());
			//blob.uploadFromFile(blob.getBlobUrl());
		});

		System.out.println("Upload complete.");
	}
}

