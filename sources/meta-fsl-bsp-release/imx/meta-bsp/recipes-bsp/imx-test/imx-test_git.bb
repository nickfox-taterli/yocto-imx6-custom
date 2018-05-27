# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017 NXP

include recipes-bsp/imx-test/imx-test.inc

DEPENDS_mx6sl += "virtual/libvpu"
DEPENDS_mx6sx += "virtual/libvpu"
DEPENDS_mx6ul += "virtual/libvpu"
DEPENDS_mx6sll += "virtual/libvpu"
DEPENDS_mx7d  += "virtual/libvpu"
DEPENDS_mx7ulp  = "virtual/kernel imx-lib virtual/libvpu"

PLATFORM_mx6sll = "IMX6SL"
PLATFORM_mx7ulp  = "IMX7D"

PARALLEL_MAKE="-j 1"

NXP_REPO_MIRROR ?= "nxp/"
SRCBRANCH = "${NXP_REPO_MIRROR}imx_4.9.11_1.0.0_ga"
IMXTEST_SRC ?= "git://source.codeaurora.org/external/imx/imx-test.git;protocol=https"

SRC_URI = "${IMXTEST_SRC};branch=${SRCBRANCH}"
SRCREV = "fb250a795ce0d25c19610e9e19e1cd815fc64cb9"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(mx6|mx7)"
