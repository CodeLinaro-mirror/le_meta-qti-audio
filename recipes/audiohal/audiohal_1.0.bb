inherit autotools-brokensep pkgconfig

DESCRIPTION = "audiohal"
SECTION = "multimedia"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI  = "file://vendor/qcom/opensource/audio-hal/primary-hal/"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/audio-hal/primary-hal"
PR = "r0"
HALBINSUFFIX = "${@bb.utils.contains('TUNE_ARCH', 'aarch64', '_64bit', '', d)}"
DEPENDS = "glib-2.0 tinycompress tinyalsa expat qal acdbdata libhardware media-headers audio-utils qahw"

EXTRA_OEMAKE = "DEFAULT_INCLUDES= CPPFLAGS="-I. -I${STAGING_KERNEL_BUILDDIR}/usr/techpack/audio/include -I${STAGING_INCDIR}/surround_sound_3mic -I${STAGING_INCDIR}/sound_trigger -I${WORKSPACE}/vendor/qcom/opensource/qal -I${S}/hal-qal/audio_extn -I $(PKG_CONFIG_SYSROOT_DIR)/usr/include/acdbdata -I${S}/hal/audio_extn/ -I${STAGING_INCDIR}""
EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += "--with-glib --program-suffix=${HALBINSUFFIX}"

EXTRA_OECONF += "TARGET_SUPPORT=${BASEMACHINE}"
EXTRA_OECONF += "BOARD_SUPPORTS_QAHW=true"
EXTRA_OECONF_append = " AUDIO_FEATURE_ENABLED_INSTANCE_ID=true"
EXTRA_OECONF_append = " AUDIO_FEATURE_ENABLED_QAHW_1_0=true"

do_install_append() {
   if [ -d "${WORKDIR}/${BASEMACHINE}" ] && [ $(ls -1  ${WORKDIR}/${BASEMACHINE} | wc -l) -ne 0 ]; then
      install -d ${D}${sysconfdir}
      install -m 0755 ${WORKDIR}/${BASEMACHINE}/* ${D}${sysconfdir}/
   fi

   #create /data/audio folder
   install -m 0755 -o root -g root -d ${D}${userfsdatadir}/audio
}

FILES_${PN} += "${libdir}/audio.primary.default.so ${userfsdatadir}/*"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

RM_WORK_EXCLUDE += "${PN}"
