### 说明
1. 本样例使用了二种开源库，QRCODE和ZXING，使用时可以只选择一个
2. QRCODE的jar包在包中，也可以网络下载。但是需要使用maven安装到本地maven库。命令如下：

```
mvn install:install-file -Dfile=qrcode.jar -DgroupId=tools -DartifactId=qrcode -Dversion=0.8 -Dpackaging=jar
```

3. 建议使用ZXING
4. 在代码中直接将网络URL中的图片作了去噪处理，实际使用，可以在第一次识别失败后，进行处理，以提高识别性能。
