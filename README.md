# i.MX6Quad 移植计划

------

移植的是目前飞思卡尔主线Yocto 2.2 Morty,虽然Yocto官方已经出了Rocko了,但是道理是一样的.Yocto具备很多优点.

> * 依赖问题再也不烦恼了,集成软件变得非常简单.
> * 工作区干净,通过Patch修正源码包,移植.
> * 切换目标变得非常轻松.

当然,也有很多缺点.

> * do_fetch 时间超长,取决于番茄工具.
> * 使用Python进行管理,在上下文切换时浪费大量的资源.
> * 验证功能变得相对麻烦,通常切出工程验证后再做补丁,再继续验证.

特殊声明:

> * Qt 属于商业版权,本项目属于我兴趣所在,随便开发的.
> * 对于卡在 do_fetch 的同学,建议换个翻墙工具,或者使用海外机器先拉取软件包.所有软件包总和约18G(20000+文件).
> * 对于RAM小于4G的同学,建议单线程编译.(即虚拟机编译只分配一个CPU或物理机编译修改conf文件)
> * 对于首次编译时间,在4个小时以上也是正常的,最好配备SSD,这样会有不少提升.
> * 编译参考教程:https://www.lijingquan.net/archives/4489
> * EMMC烧写工具:IMX6_L5.1_2.1.0_MFG_TOOL.tar 参考下载地址:https://www.nxp.com/webapp/sps/download/license.jsp?colCode=IMX_6DQ_MFG_TOOL

网友制作的离线软件包文件,解压后放到DL_DIR指向的路径,就可以实现无网络编译.

[![Download yocto-imx6-custom](https://a.fsdn.com/con/app/sf-download-button)](https://sourceforge.net/projects/yocto-imx6-custom/files/latest/download)

------

## 系统环境

 1. 宿主机:最好不是VM,系统必须为Ubuntu 16.04 x64,最好不是中文版的.
 2. 最好准备睡一觉.

## HDMI测试

 1. 插入HDMI并给板子上电.
 2. 开机后启动U-Boot阶段中断启动,然后附加启动参数 video=mxcfb0:dev=hdmi,if=RGB24,bpp=24.
 3. HDMI开机后可用.

## 网络测试

 1. 插入网线.
 2. 运行udhcpc -i eth0.
 3. 获取到IP并尝试ping路由,通过即可.

## USB口测试

 1. 插U盘,看看能不能挂载.
 2. 插鼠标,看看能不能动.

## 按键测试

 1. 运行evtest测试.
 2. 左右按键是加减音量,电源按键是电源按钮.

## 红外测试

 1. 运行evtest测试.
 2. 使用淘宝便宜遥控器,下面丝印是Car. MP3,上面有CH-,CH,CH+等按钮.
 3. 红外不支持连发

## SATA 测试

 1. 硬件接上,电源共地,电源可外接,就可以挂载了.
 2. 可测试读写确认情况.

## RGB接口

 1. 800*480 FT5216 触摸屏.
 2. 可触摸,显示即为正常.

## 音频接口

 1. 可以播放,音量正常.
 2. 录音无硬件测试,目前看来正常.(PATH依修复)

## EMMC烧录

 1. bitbake fsl-image-mfgtool-initramfs
 2. 相关文件复制出来就可以用,ucl.xml文件mkfs加上-F参数,mksdcard.sh.tar需要取消-uM参数并在下面声明,具体看我博客.

## EMMC文件复制

```shell
    cp tmp/deploy/images/imx6qsabresd/u-boot.imx-mfgtool mfg/firmware/u-boot-imx6qsabresd_sd.imx 
    cp tmp/deploy/images/imx6qsabresd/zImage_mfgtool mfg/firmware/zImage
    cp tmp/deploy/images/imx6qsabresd/fsl-image-mfgtool-initramfs-imx6qsabresd.cpio.gz.u-boot mfg/firmware/fsl-image-mfgtool-initramfs-imx_mfgtools.cpio.gz.u-boot
    cp tmp/deploy/images/imx6qsabresd/zImage-zImage-mfgtool-imx6q-sabresd.dtb mfg/firmware/zImage-imx6q-sabresd.dtb
    cp tmp/deploy/images/imx6qsabresd/u-boot-imx6qsabresd.imx-sd mfg/files/u-boot-imx6qsabresd_sd.imx
    cp tmp/deploy/images/imx6qsabresd/zImage mfg/files/zImage
    cp tmp/deploy/images/imx6qsabresd/zImage-imx6q-sabresd.dtb mfg/files/zImage-imx6q-sabresd.dtb
    cp tmp/deploy/images/imx6qsabresd/fsl-image-qt5-imx6qsabresd.tar.bz2 mfg/files/rootfs.tar.bz2
```
