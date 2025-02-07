import { defineConfig } from 'vite';

export default defineConfig({
    test: {
        globals: true,
        environment: "jsdom",
        setupFiles: "./vitest.setup.ts",
        css: false,
        coverage: {
            provider: 'v8',
            reporter: ['text', 'json', 'html'],
        },
    }
})
