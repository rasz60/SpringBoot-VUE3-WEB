const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  // outputDir: npm run build 로 빌드시 파일이 생성되는 위치
  outputDir: "../src/main/resources/static",
  transpileDependencies: true,
  indexPath: "index.html",
  devServer: {
    proxy: {
      "/rest": {
        target: "http://localhost:8082",
        changeOrigin: true,
      },
    },
  },
});
