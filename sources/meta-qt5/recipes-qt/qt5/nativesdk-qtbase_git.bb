DESCRIPTION = "SDK version of Qt/[X11|Mac|Embedded]"
DEPENDS = "nativesdk-zlib qtbase-native"
SECTION = "libs"
HOMEPAGE = "http://qt-project.org"

LICENSE = "GFDL-1.3 & BSD & ( GPL-3.0 & The-Qt-Company-GPL-Exception-1.0 | The-Qt-Company-Commercial ) & ( GPL-2.0+ | LGPL-3.0 | The-Qt-Company-Commercial )"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPL3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
    file://LICENSE.LGPLv21;md5=fb91571854638f10b2e5f36562661a5a \
    file://LICENSE.LGPLv3;md5=a909b94c1c9674b2aa15ff03a86f518a \
    file://LICENSE.GPL2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://LICENSE.GPL3;md5=d32239bcb673463ab874e80d47fae504 \
    file://LICENSE.GPL3-EXCEPT;md5=763d8c535a234d9a3fb682c7ecb6c073 \
    file://LICENSE.GPLv3;md5=88e2b9117e6be406b5ed6ee4ca99a705 \
    file://LGPL_EXCEPTION.txt;md5=9625233da42f9e0ce9d63651a9d97654 \
    file://LICENSE.FDL;md5=6d9f2a9af4c8b8c3c769f6cc1b6aaf7e \
"

QT_MODULE = "qtbase"

require nativesdk-qt5.inc
require qt5-git.inc

# it's already included with newer oe-core, but include it here for dylan
FILESEXTRAPATHS =. "${FILE_DIRNAME}/qtbase:"

# common for qtbase-native, qtbase-nativesdk and qtbase
SRC_URI += "\
    file://0001-Add-linux-oe-g-platform.patch \
    file://0002-qlibraryinfo-allow-to-set-qt.conf-from-the-outside-u.patch \
    file://0003-Add-external-hostbindir-option.patch \
    file://0004-qt_module-Fix-pkgconfig-and-libtool-replacements.patch \
    file://0005-configure-bump-path-length-from-256-to-512-character.patch \
    file://0009-Disable-all-unknown-features-instead-of-erroring-out.patch \
    file://0010-Pretend-Qt5-wasn-t-found-if-OE_QMAKE_PATH_EXTERNAL_H.patch \
"

# common for qtbase-native and nativesdk-qtbase
SRC_URI += " \
    file://0011-Always-build-uic.patch \
"

# CMake's toolchain configuration of nativesdk-qtbase
SRC_URI += " \
    file://OEQt5Toolchain.cmake \
"

PACKAGES = "${PN}-tools-dbg ${PN}-tools-dev ${PN}-tools-staticdev ${PN}-tools"

PACKAGE_DEBUG_SPLIT_STYLE = "debug-without-src"

FILES_${PN}-tools-dev = " \
    ${includedir} \
    ${FILES_SOLIBSDEV} ${libdir}/*.la \
    ${libdir}/*.prl \
    ${OE_QMAKE_PATH_ARCHDATA}/mkspecs \
    ${OE_QMAKE_PATH_LIBS}/*.prl \
"

FILES_${PN}-tools-staticdev = " \
    ${OE_QMAKE_PATH_LIBS}/*.a \
"

FILES_${PN}-tools-dbg = " \
    ${libdir}/.debug \
    ${OE_QMAKE_PATH_BINS}/.debug \
"

FILES_${PN}-tools = " \
    ${libdir}/lib*${SOLIBS} \
    ${OE_QMAKE_PATH_BINS}/* \
    ${SDKPATHNATIVE}/environment-setup.d \
    ${datadir}/cmake \
"

# qttools binaries are placed in a subdir of bin in order to avoid
# collisions with qt4. This would trigger debian.bbclass to rename the
# package, since it doesn't detect binaries in subdirs. Explicitly
# disable package auto-renaming for the tools-package.
DEBIAN_NOAUTONAME_${PN}-tools = "1"

QT_CONFIG_FLAGS += " \
    -shared \
    -silent \
    -no-pch \
    -no-rpath \
    -pkg-config \
    ${PACKAGECONFIG_CONFARGS} \
"

# qtbase is exception, as these are used as install path for sysroots
OE_QMAKE_PATH_HOST_DATA = "${libdir}${QT_DIR_NAME}"
OE_QMAKE_PATH_HOST_LIBS = "${libdir}"

# for qtbase configuration we need default settings
# since we cannot set empty set filename to a not existent file
export OE_QMAKE_QTCONF_PATH = "foodummy"

do_configure() {
    ${S}/configure -v \
        -opensource -confirm-license \
        -sysroot ${STAGING_DIR_TARGET} \
        -no-gcc-sysroot \
        -system-zlib \
        -dbus-runtime \
        -no-libjpeg \
        -no-libpng \
        -no-gif \
        -no-accessibility \
        -no-cups \
        -no-gui \
        -no-qml-debug \
        -no-sql-mysql \
        -no-sql-sqlite \
        -no-opengl \
        -no-openssl \
        -no-xcb \
        -no-icu \
        -verbose \
        -release \
        -prefix ${OE_QMAKE_PATH_PREFIX} \
        -bindir ${OE_QMAKE_PATH_BINS} \
        -libdir ${OE_QMAKE_PATH_LIBS} \
        -datadir ${OE_QMAKE_PATH_DATA} \
        -sysconfdir ${OE_QMAKE_PATH_SETTINGS} \
        -docdir ${OE_QMAKE_PATH_DOCS} \
        -headerdir ${OE_QMAKE_PATH_HEADERS} \
        -archdatadir ${OE_QMAKE_PATH_ARCHDATA} \
        -libexecdir ${OE_QMAKE_PATH_LIBEXECS} \
        -plugindir ${OE_QMAKE_PATH_PLUGINS} \
        -importdir ${OE_QMAKE_PATH_IMPORTS} \
        -qmldir ${OE_QMAKE_PATH_QML} \
        -translationdir ${OE_QMAKE_PATH_TRANSLATIONS} \
        -testsdir ${OE_QMAKE_PATH_TESTS} \
        -hostbindir ${OE_QMAKE_PATH_HOST_BINS} \
        -hostdatadir ${OE_QMAKE_PATH_HOST_DATA} \
        -host-option CROSS_COMPILE=${HOST_PREFIX} \
        -external-hostbindir ${OE_QMAKE_PATH_EXTERNAL_HOST_BINS} \
        -no-glib \
        -no-iconv \
        -silent \
        -nomake examples \
        -nomake tests \
        -no-compile-examples \
        -no-rpath \
        -platform ${OE_QMAKE_PLATFORM_NATIVE} \
        -xplatform ${OE_QMAKE_PLATFORM} \
        ${QT_CONFIG_FLAGS}
}

do_install() {
    qmake5_base_do_install

    # remove things unused in nativesdk, we need the headers and libs
    rm -rf ${D}${datadir} \
           ${D}/${OE_QMAKE_PATH_PLUGINS} \
           ${D}${libdir}/cmake \
           ${D}${libdir}/pkgconfig

    # Install CMake's toolchain configuration
    mkdir -p ${D}${datadir}/cmake/OEToolchainConfig.cmake.d/
    install -m 644 ${WORKDIR}/OEQt5Toolchain.cmake ${D}${datadir}/cmake/OEToolchainConfig.cmake.d/
}

fakeroot do_generate_qt_environment_file() {
    mkdir -p ${D}${SDKPATHNATIVE}/environment-setup.d/
    script=${D}${SDKPATHNATIVE}/environment-setup.d/qt5.sh

    echo 'export PATH=${OE_QMAKE_PATH_HOST_BINS}:$PATH' > $script
    echo 'export OE_QMAKE_CFLAGS="$CFLAGS"' >> $script
    echo 'export OE_QMAKE_CXXFLAGS="$CXXFLAGS"' >> $script
    echo 'export OE_QMAKE_LDFLAGS="$LDFLAGS"' >> $script
    echo 'export OE_QMAKE_CC=$CC' >> $script
    echo 'export OE_QMAKE_CXX=$CXX' >> $script
    echo 'export OE_QMAKE_LINK=$CXX' >> $script
    echo 'export OE_QMAKE_AR=$AR' >> $script
    echo 'export QT_CONF_PATH=${OE_QMAKE_PATH_HOST_BINS}/qt.conf' >> $script
    echo 'export OE_QMAKE_LIBDIR_QT=`qmake -query QT_INSTALL_LIBS`' >> $script
    echo 'export OE_QMAKE_INCDIR_QT=`qmake -query QT_INSTALL_HEADERS`' >> $script
    echo 'export OE_QMAKE_MOC=${OE_QMAKE_PATH_HOST_BINS}/moc' >> $script
    echo 'export OE_QMAKE_UIC=${OE_QMAKE_PATH_HOST_BINS}/uic' >> $script
    echo 'export OE_QMAKE_RCC=${OE_QMAKE_PATH_HOST_BINS}/rcc' >> $script
    echo 'export OE_QMAKE_QDBUSCPP2XML=${OE_QMAKE_PATH_HOST_BINS}/qdbuscpp2xml' >> $script
    echo 'export OE_QMAKE_QDBUSXML2CPP=${OE_QMAKE_PATH_HOST_BINS}/qdbusxml2cpp' >> $script
    echo 'export OE_QMAKE_QT_CONFIG=`qmake -query QT_INSTALL_LIBS`${QT_DIR_NAME}/mkspecs/qconfig.pri' >> $script
    echo 'export OE_QMAKE_PATH_HOST_BINS=${OE_QMAKE_PATH_HOST_BINS}' >> $script
    echo 'export QMAKESPEC=`qmake -query QT_INSTALL_LIBS`${QT_DIR_NAME}/mkspecs/linux-oe-g++' >> $script

    # Use relocable sysroot
    sed -i -e 's:${SDKPATHNATIVE}:$OECORE_NATIVE_SYSROOT:g' $script
}

addtask generate_qt_environment_file after do_install before do_package

SRCREV = "49dc9aa409d727824f26b246054a22b5a7dd5980"
