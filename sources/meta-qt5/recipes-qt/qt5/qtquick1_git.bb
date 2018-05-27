require qt5.inc
require qt5-git.inc

LICENSE = "GFDL-1.3 & BSD & ( GPL-3.0 & The-Qt-Company-GPL-Exception-1.0 | The-Qt-Company-Commercial ) & ( GPL-2.0+ | LGPL-3.0 | The-Qt-Company-Commercial )"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv21;md5=4bfd28363f541b10d9f024181b8df516 \
    file://LICENSE.LGPLv3;md5=e0459b45c5c4840b353141a8bbed91f0 \
    file://LICENSE.GPLv2;md5=c96076271561b0e3785dad260634eaa8 \
    file://LGPL_EXCEPTION.txt;md5=9625233da42f9e0ce9d63651a9d97654 \
    file://LICENSE.FDL;md5=6d9f2a9af4c8b8c3c769f6cc1b6aaf7e \
"

DEPENDS += "qtscript qtsvg qtxmlpatterns"
# qttools

PACKAGECONFIG ??= "webkit"
PACKAGECONFIG[webkit] = ",,qtwebkit"

EXTRA_QMAKEVARS_PRE += "${@bb.utils.contains('PACKAGECONFIG', 'webkit', '', 'CONFIG+=noqtwebkit', d)}"

do_configure_prepend() {
    sed -i 's#^qtHaveModule(webkitwidgets):#qtHaveModule(webkitwidgets):!contains(CONFIG, noqtwebkit):#g' ${S}/src/imports/imports.pro
}

QT_MODULE_BRANCH = "dev"
SRCREV = "64faeb0d8003e699a4d09e7dcee1ef6eb10302ad"
