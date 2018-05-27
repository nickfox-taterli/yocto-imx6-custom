require qt5.inc
require qt5-git.inc

LICENSE = "LGPL-3.0 | GPL-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv3;md5=8211fde12cc8a4e2477602f5953f5b71 \
    file://LICENSE.GPLv3;md5=88e2b9117e6be406b5ed6ee4ca99a705 \
    file://LICENSE.GPL;md5=c96076271561b0e3785dad260634eaa8 \
"

DEPENDS += "qtbase"
DEPENDS_class-target += "qtdeclarative qt3d-native"

SRC_URI += " \
    file://0001-Allow-a-tools-only-build.patch \
"

PACKAGECONFIG ??= ""
PACKAGECONFIG_class-native ??= "tools-only"
PACKAGECONFIG_class-nativesdk ??= "tools-only"
PACKAGECONFIG[tools-only] = ""
PACKAGECONFIG[system-assimp] = "-feature-system-assimp,-no-feature-system-assimp,assimp"
PACKAGECONFIG[qtgamepad] = ",,qtgamepad"

EXTRA_QMAKEVARS_CONFIGURE += "${PACKAGECONFIG_CONFARGS}"

EXTRA_QMAKEVARS_PRE += "${@bb.utils.contains('PACKAGECONFIG', 'tools-only', 'CONFIG+=tools-only QMAKE_USE_PRIVATE+=zlib', '', d)}"
EXTRA_QMAKEVARS_PRE += "${@bb.utils.contains('PACKAGECONFIG', 'qtgamepad', 'CONFIG+=OE_QTGAMEPAD_ENABLED', '', d)}"

do_configure_prepend() {
    # disable qtgamepad test if it isn't enabled by PACKAGECONFIG
    sed -e 's/^\(qtHaveModule(gamepad)\)/OE_QTGAMEPAD_ENABLED:\1/' -i \
         ${S}/src/input/frontend/frontend.pri \
         ${S}/src/quick3d/imports/input/importsinput.pro
}

SRCREV = "440589a0747d9668fec3ff924b390d75be5c6733"

BBCLASSEXTEND += "native nativesdk"
