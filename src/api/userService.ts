/* eslint-disable no-console */
import request from "@/api/request";

class UserService {
  public chunkedArr: any[] = [];

  save(data: FormData) {
    return request({
      method: "post",
      url: "/user/save",
      data: data,
      //headers: {
      //"content-type": "application/json",
      //"content-type": "multipart/form-data",
      //},
      responseType: "json",
      timeout: 60000 * 30,
      onDownloadProgress: (progressEvent) => {
        const dataChunk = progressEvent.currentTarget.response;
        //console.dir(progressEvent);
        //const loadingValue = Math.floor((progressEvent.loaded / progressEvent.total) * 100);
        const loadingValue = Math.floor((progressEvent.loaded / 100) * 100);
        console.log(loadingValue);
        console.log(JSON.stringify(dataChunk));
        this.chunkedArr.push(progressEvent);
      },
    });
  }
}

export default new UserService();
