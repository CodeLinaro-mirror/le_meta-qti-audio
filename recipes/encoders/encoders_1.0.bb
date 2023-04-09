inherit autotools-brokensep pkgconfig

DESCRIPTION = "encoders"
LICENSE = "BSD-Source-Code"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=fe8b41221d7524c70688f7d059ff6d87"

PR = "r0"

FILESPATH   =+ "${WORKSPACE}/:"
SRC_URI     =  "file://hardware/qcom/audio/mm-audio/"

S = "${WORKDIR}/hardware/qcom/audio/mm-audio/"
AUDIO_KERNEL_HEADERS="${STAGING_KERNEL_BUILDDIR}/audio-kernel"
AUDIO_KERNEL_HEADERS:qrbx210="${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio"
AUDIO_KERNEL_HEADERS:kona="${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio"
AUDIO_KERNEL_HEADERS:sdmsteppe="${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio"
CFLAGS += "-I${AUDIO_KERNEL_HEADERS}"

EXTRA_OECONF:append += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF:append += "--with-glib"
EXTRA_OECONF:append = " --with-audio-kernel-headers=${AUDIO_KERNEL_HEADERS}"

DEPENDS = "glib-2.0 media liblog libcutils libutils"
DEPENDS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'audio-dlkm', ' audiodlkm', '', d)}"

do_configure[depends] += "audiodlkm:do_install"

RDEPENDS:${PN} = "media"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
