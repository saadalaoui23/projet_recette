import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  
  // 🟢 AJOUTEZ CE BLOC pour configurer le proxy
  server: {
    // Redirige toutes les requêtes commençant par /api vers la Gateway
    proxy: {
      '/api': {
        target: 'http://localhost:8000', // <-- Le port local de votre API Gateway
        changeOrigin: true,
        // (Optionnel) Réécrit l'URL pour retirer /api si la Gateway l'attend
        // rewrite: (path) => path.replace(/^\/api/, ''), 
      },}}
})
