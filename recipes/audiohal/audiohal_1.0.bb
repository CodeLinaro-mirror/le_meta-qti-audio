inherit autotools-brokensep pkgconfig

DESCRIPTION = "audiohal"
SECTION = "multimedia"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI  = "file://hardware/qcom/audio/"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/hardware/qcom/audio/"
PR = "r0"
HALBINSUFFIX = "${@bb.utils.contains('TUNE_ARCH', 'aarch64', '_64bit', '', d)}"

DEPENDS += "glib-2.0 tinycompress tinyalsa expat libhardware acdbloader surround-sound-3mic qahw media-headers audio-utils audio-route"
DEPENDS_append_apq8098 = " audio-qaf audio-parsers audio-qap-wrapper audio-ip-handler"
DEPENDS_append_apq8009 = " ffv qti-audio-server binder"
DEPENDS_append_apq8017 = " ffv qti-audio-server binder"
DEPENDS_append_apq8053 = " qti-audio-server binder"
DEPENDS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'audio-dlkm', ' audiodlkm', '', d)}"

#apq8098 doesn't need surround sound recording
DEPENDS_remove_apq8098 = "surround-sound-3mic"
#sdxprairie doesn't need surround sound recording
DEPENDS_remove_sdxprairie = "surround-sound-3mic"

do_configure[depends] += "audiodlkm:do_install"

AUDIO_KERNEL_HEADERS="${STAGING_KERNEL_BUILDDIR}/audio-kernel"
CFLAGS += "-I${AUDIO_KERNEL_HEADERS}"

EXTRA_OEMAKE = "DEFAULT_INCLUDES= CPPFLAGS="-I. -I${STAGING_KERNEL_BUILDDIR}/usr/include -I${STAGING_KERNEL_BUILDDIR}/usr/techpack/audio/include -I${STAGING_INCDIR}/surround_sound_3mic -I${STAGING_INCDIR}/sound_trigger""
EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += "--with-glib --program-suffix=${HALBINSUFFIX}"
EXTRA_OECONF += " --with-audio-kernel-headers=${AUDIO_KERNEL_HEADERS}"

EXTRA_OECONF += "TARGET_SUPPORT=${BASEMACHINE}"
EXTRA_OECONF += "BOARD_SUPPORTS_QAHW=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_HFP=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_MULTI_VOICE_SESSIONS=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_SPKR_PROTECTION=true"
EXTRA_OECONF += "MULTIPLE_HW_VARIANTS_ENABLED=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_SOURCE_TRACKING=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_PM_SUPPORT=false"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_EXTN_AMR_DECODER=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_MULTI_RECORD=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_PROXY_DEVICE=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_QAHW_1_0=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_INSTANCE_ID=true"
EXTRA_OECONF += "AUDIO_FEATURE_ADSP_HDLR_ENABLED=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_DTMF=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_AFE_LOOPBACK=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_KEEP_ALIVE=true"
EXTRA_OECONF += "AUDIO_FEATURE_ENABLED_SND_MONITOR=true"

do_install_append() {
   if [ -d "${WORKDIR}/${BASEMACHINE}" ] && [ $(ls -1  ${WORKDIR}/${BASEMACHINE} | wc -l) -ne 0 ]; then
      install -d ${D}${sysconfdir}
      install -m 0755 ${WORKDIR}/${BASEMACHINE}/* ${D}${sysconfdir}/
   fi

   #create /data/audio folder
   install -m 770 -d ${D}${userfsdatadir}/audio
}

FILES_${PN} += "${libdir}/audio.primary.default.so ${userfsdatadir}/*"
FILES_${PN} += "${libdir}/audio.compress.capture.so ${userfsdatadir}/*"
FILES_${PN} += "${libdir}/audio.hdmi.edid.so ${userfsdatadir}/*"
FILES_${PN} += "${libdir}/audio.spkr.prot.so ${userfsdatadir}/*"
FILES_${PN} += "${libdir}/audio.a2dp.offload.so ${userfsdatadir}/*"
FILES_${PN} += "${libdir}/audio.snd.monitor.so ${userfsdatadir}/*"
FILES_${PN} += "${libdir}/audio.ssrec.so ${userfsdatadir}/*"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
