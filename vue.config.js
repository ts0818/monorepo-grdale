const path = require("path");
const resolve = (dir) => path.join(__dirname, dir);
const vueSrc = "src/";
module.exports = {
  //settings: {
  //  "vetur.useWorkspaceDependencies": true,
  //  "vetur.experimental.templateInterpolationService": true,
  //},
  css: {
    loaderOptions: {
      scss: {
        additionalData: `
          @use "@/assets/common/scss/color.scss";
          @use "@/assets/common/scss/feature.scss";
         `,
      },
    },
  },
  // chainWebpack: (config) => {
  //   config.resolve.alias
  //     .set("@", resolve("src"))
  //     .set("assets", resolve("src/assets"))
  //     .set("components", resolve("src/components"))
  //     .set("views", resolve("src/views"))
  //     .set("modules", resolve(vueSrc.concat("modules")));
  //   config.resolve.extensions
  //     .merge([".mjs", ".js", ".jsx", ".vue", ".json", ".wasm", ".ts"])
  //     .end();
  // },
};
