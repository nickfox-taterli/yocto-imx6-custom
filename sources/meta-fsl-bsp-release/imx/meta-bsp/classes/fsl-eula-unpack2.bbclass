# fsl-eula-unpack2.bbclass provides a mechanism for a) unpacking certain
# EULA-licensed archives downloaded by HTTP and b) handling the EULA
# acceptance.

inherit fsl-eula-unpack fsl-eula

IMX_PACKAGE_VERSION = "${PV}"

SRC_URI = "${FSL_MIRROR}${IMX_PACKAGE_NAME}.bin;name=${SRC_URI_NAME};fsl-eula=true"

S = "${WORKDIR}/${IMX_PACKAGE_NAME}"

# For native apps, insert the user-local sysroot path
# For nativesdk apps, insert the correct distro folder
D_SUBDIR                 = ""
D_SUBDIR_class-native    = "${STAGING_DIR_NATIVE}"
D_SUBDIR_class-nativesdk = "/opt/${DISTRO}"

do_install () {
    install -d ${D}${D_SUBDIR}
    cp -r ${S}/* ${D}${D_SUBDIR}
    rm ${D}${D_SUBDIR}/COPYING
}

FILES_${PN} = "/"
