inherit autotools update-rc.d systemd

DESCRIPTION = "Installing audio init script"
LICENSE = "BSD-Source-Code"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=fe8b41221d7524c70688f7d059ff6d87"
PR = "r5"

DEPENDS:append_mdm9635 +="alsa-intf"

SRC_URI = "file://init_qcom_audio"
SRC_URI += "file://init_audio.service"
SRC_URI += "file://msm-audio-node.rules"

do_compile[noexec] = "1"

S = "${WORKDIR}"

INITSCRIPT_NAME = "init_qcom_audio"
INITSCRIPT_PARAMS = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME:apq8009 = "init_qcom_audio"
INITSCRIPT_PARAMS:apq8009 = "start 38 2 3 4 5 . stop 1 0 1 6 ."

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${S}/msm-audio-node.rules -D ${D}${sysconfdir}/udev/rules.d/msm-audio-node.rules
        install -m 0644 ${S}/init_audio.service -D ${D}${systemd_unitdir}/system/init_audio.service
        install -d ${D}/${systemd_unitdir}/system/sysinit.target.wants
        ln -sf ${systemd_unitdir}/system/init_audio.service ${D}${systemd_unitdir}/system/sysinit.target.wants/init_audio.service
        echo "\
        # Create directory in /data/audio for location with audio:audio permissions
        d /data/audio 0755 audio audio - -
        # Change selinux context of new directory. Use Z to apply for subdirectories as well.
        T /data/audio - - - - security.selinux="system_u:object_r:audio_data_file_t:s0"
        " > ${WORKDIR}/${BPN}.conf
        ## Install systemd-tmpfiles config file
        install -d ${D}${sysconfdir}/tmpfiles.d/
        install -m 0644 ${WORKDIR}/${BPN}.conf ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf
    else
        install -m 0755 ${S}/init_qcom_audio -D ${D}${sysconfdir}/init.d/init_qcom_audio
    fi

}

FILES:${PN} += "${systemd_unitdir}/system/*"
