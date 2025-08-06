const nextJest = require('next/jest');

const createJestConfig = nextJest({
  dir: './',
});

var ignoredModules = [
  'react-markdown',
  'vfile',
  'unist-util-stringify-position',
  'unified',
  'bail',
  'is-plain-obj',
  'trough',
  'remark-parse',
  'mdast-util-from-markdown',
  'mdast-util-to-string',
  'micromark',
  'decode-named-character-reference',
  'character-entities',
  'remark-rehype',
  'mdast-util-to-hast',
  'unist-builder',
  'unist-util-visit',
  'unist-util-is',
  'unist-util-position',
  'unist-util-generated',
  'mdast-util-definitions',
  'trim-lines',
  'property-information',
  'hast-util-whitespace',
  'space-separated-tokens',
  'comma-separated-tokens',
  'remark-gfm',
  'mdast-util-gfm',
  'mdast-util-gfm-autolink-literal',
  'mdast-util-find-and-replace',
  'mdast-util-to-markdown',
  'markdown-table',
  'escape-string-regexp',
  'ccount',
  'hast-util-raw',
  'rehype-raw',
  'hast-util-from-parse5',
  'hast-util-to-parse5',
  'hastscript',
  'hast-util-parse-selector',
  'web-namespaces',
  'zwitch',
  'html-void-elements',
  'devlop',
  'hast-util-to-jsx-runtime',
  'estree-util-is-identifier-name',
  'html-url-attributes',
  'longest-streak',
  'mdast-util-phrasing',
];

const customJestConfig = {
  setupFilesAfterEnv: ['<rootDir>/jest.setup.js'],
  moduleNameMapper: {
    '^@/components/(.*)$': '<rootDir>/src/components/$1',
    '^@/pages/(.*)$': '<rootDir>/pages/$1',
    '^@/app/(.*)$': '<rootDir>/src/app/$1',
    '^@/(.*)$': '<rootDir>/src/$1',
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy'
  },
  testEnvironment: 'jest-environment-jsdom',
  coverageProvider: 'v8',
  collectCoverage: true,
  transform: {
    '^.+\\.(js|jsx|ts|tsx)$': 'babel-jest',
  },
  transformIgnorePatterns: [`node_modules/(?!(.pnpm/)?(${ignoredModules.join('|')})).*`],
  collectCoverageFrom: [
    'src/**/*.{js,jsx,ts,tsx}',
  ],
  setupFiles: ['<rootDir>/setup.jest.ts'],
  globals: {
    Uint8Array: Uint8Array,
  },
  coverageThreshold: {
    global: {
      branches: 90,
      functions: 90,
      lines: 90,
      statements: 90,
    }
  },
  coveragePathIgnorePatterns: [
    "/node_modules/",
    "loading.tsx$",
    "firebase.ts$",
    "/src/components/ui/",
  ],
};

module.exports = async () => ({
  ...(await createJestConfig(customJestConfig)()),
  transformIgnorePatterns: [`node_modules/(?!(.pnpm/)?(${ignoredModules.join('|')})).*`],
});
