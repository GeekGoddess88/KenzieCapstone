const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
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
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/inventory-management.html',
      filename: 'inventory-management.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/menu-management.html',
      filename: 'menu-management.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/employee-portal.html',
      filename: 'employee-portal.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/menu.html',
      filename: 'menu.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/barista-menu.html',
      filename: 'barista-menu.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
