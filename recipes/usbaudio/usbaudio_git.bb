inherit autotools pkgconfig

DESCRIPTION = "usbaudio"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESPATH =+ "${WORKSPACE}:"

SRC_URI   = "file://hardware/libhardware/modules/usbaudio/"
S = "${WORKDIR}/hardware/libhardware/modules/usbaudio/"

PR = "r0"

DEPENDS = "tinyalsa system-media libhardware"

FILES:${PN} += "${libdir}/*.so"
INSANE_SKIP:${PN} = "dev-deps"
