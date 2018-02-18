# 【开源项目】将图片转换为字符画 #

## 原理 ##
- 选定填充图片的ASCII字符，不同的字符对应于不同的灰度
- 读取图片并计算各像素灰度值（同时考虑透明背景），用相应的的ASCII字符替换该像素

## 程序功能 ##
- 支持3种文件选择方式：选定文件（支持图片预览），添加文件夹，拖入文件
- 支持5种图片格式：.jpg， .jpeg， .gif， .png，.bmp
- 支持5挡不同的缩放比例：10%，20%，25%，50%，以及不缩放，默认为不缩放
- 转换结果以文件名“原文件名+.txt”保存至新建文件夹，新建文件夹的命名方式为“字符画转换结果+当前时间”，其中当前时间的格式为“年\_月\_日\_时\_分\_秒”

建议：

- 转换后的txt文件最好用notepad++等类似软件打开，这类软件不会将内容换行，同时支持缩放
- 图片宽和高最好控制在1000px以内，太大的话，出来的图太过精细，不方便查看。这时可利用缩放功能。

## 可视化界面 ##
- 为方便操作，特意包装成可视化界面，并加入“保持窗口最前”选项，方便文件拖入
- 注意：如果所选文件格式不正确，“开始转换”按钮不可用
- 程序已打包为exe文件64位版本，可以直接使用

## 源码和exe文件 ##
- source文件夹：源码及其资源文件
- ExecuteFileAndPackageTools文件夹：可执行文件Img2Ascii.exe及其打包文件

## 软件截图和效果图 ##
软件截图：
![image](https://github.com/xiaoxi666/Img2AsciiVision/blob/master/Demos/Img2Ascii.exe.png)
原图：
![image](https://github.com/xiaoxi666/Img2AsciiVision/blob/master/Demos/img.jpg)
转换为Ascii后的字符画：
![image](https://github.com/xiaoxi666/Img2AsciiVision/blob/master/Demos/img_Ascii.jpg)
局部放大图（可以看到Ascii码）：
![image](https://github.com/xiaoxi666/Img2AsciiVision/blob/master/Demos/ZoomIn.jpg)
