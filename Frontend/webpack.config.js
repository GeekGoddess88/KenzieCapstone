const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true,
  },
  entry: {
    inventoryManagementPage: path.resolve(__dirname, 'src', 'pages', 'inventoryManagementPage.js'),
    menuManagementPage: path.resolve(__dirname, 'src', 'pages', 'menuManagementPage.js'),
    employeePortalPage: path.resolve(__dirname, 'src', 'pages', 'employeePortalPage.js'),
    menuPage: path.resolve(__dirname, 'src', 'pages', 'menuPage.js'),
    baristaMenuPage: path.resolve(__dirname, 'src', 'pages', 'baristaMenuPage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
    publicPath: '/',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    proxy: [
      {
        context: [
          '/ingredients',
          '/drinks'
        ],
        target: 'http://localhost:5001'
      },
    ],
  },
  module: {
    rules: [
      {
        test: /\.css$/i,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      chunks: [],
      inject: true,
    }),
    new HtmlWebpackPlugin({
      template: './src/inventory-management.html',
      filename: 'inventory-management.html',
      chunks: ['inventoryManagementPage'],
      inject: true,
    }),
    new HtmlWebpackPlugin({
      template: './src/menu-management.html',
      filename: 'menu-management.html',
      chunks: ['menuManagementPage'],
      inject: true,
    }),
    new HtmlWebpackPlugin({
      template: './src/employee-portal.html',
      filename: 'employee-portal.html',
      chunks: ['employeePortalPage'],
      inject: true,
    }),
    new HtmlWebpackPlugin({
      template: './src/menu.html',
      filename: 'menu.html',
      chunks: ['menuPage'],
      inject: true,
    }),
    new HtmlWebpackPlugin({
      template: './src/barista-menu.html',
      filename: 'barista-menu.html',
      chunks: ['baristaMenuPage'],
      inject: true
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        },
      ],
    }),
    new CleanWebpackPlugin(),
  ],
};
