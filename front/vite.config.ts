import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  // Proxy를 활용하여 특정 경로에 대한 라우팅 가능
  server: {
    proxy: {
      "/api": {target : "http://localhost:8080",
        rewrite: (path) => path.replace(/^\/api/, ""),
      }
    }
  }
});
