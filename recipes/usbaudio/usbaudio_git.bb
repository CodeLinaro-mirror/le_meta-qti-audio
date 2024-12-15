inherit autotools pkgconfig

DESCRIPTION = "usbaudio"
LICENSE = "BSD-Source-Code"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=fe8b41221d7524c70688f7d059ff6d87"

FILESPATH =+ "${WORKSPACE}:"

SRC_URI   = "file://hardware/libhardware/modules/usbaudio/"
S = "${WORKDIR}/hardware/libhardware/modules/usbaudio/"

PR = "r0"

DEPENDS = "tinyalsa system-media libhardware"

FILES:${PN} += "${libdir}/*.so"
INSANE_SKIP:${PN} = "dev-deps"
