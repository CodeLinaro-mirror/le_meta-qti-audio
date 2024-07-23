inherit autotools update-rc.d systemd

DESCRIPTION = "Installing audio init script"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
PR = "r5"

DEPENDS:append:mdm9635 +="alsa-intf"

SRC_URI = "file://init_qcom_audio"
SRC_URI += "file://init_audio.service"
SRC_URI += "file://msm-audio-node.rules"
SRC_URI += "file://${BASEMACHINE}/"

do_compile[noexec] = "1"

S = "${WORKDIR}"

INITSCRIPT_NAME = "init_qcom_audio"
INITSCRIPT_PARAMS = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME:apq8009 = "init_qcom_audio"
INITSCRIPT_PARAMS:apq8009 = "start 38 2 3 4 5 . stop 1 0 1 6 ."

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${S}/msm-audio-node.rules -D ${D}${sysconfdir}/udev/rules.d/msm-audio-node.rules
        install -m 0644 ${S}/${BASEMACHINE}/init_audio.service -D ${D}${systemd_unitdir}/system/init_audio.service
        install -d ${D}/${systemd_unitdir}/system/sysinit.target.wants
        install -d ${D}${sysconfdir}/initscripts
        install -m 0555 ${WORKDIR}/${BASEMACHINE}/start_audio_le ${D}${sysconfdir}/initscripts
        ln -sf ${systemd_unitdir}/system/init_audio.service ${D}${systemd_unitdir}/system/sysinit.target.wants/init_audio.service
        if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
            echo "\
            # Change selinux context of new directory. Use Z to apply for subdirectories as well.
            T /data/audio - - - - security.selinux="system_u:object_r:audio_data_file_t:s0"
            " > ${WORKDIR}/${BPN}.conf
        fi
        ## Install systemd-tmpfiles config file
        install -d ${D}${sysconfdir}/tmpfiles.d/
        if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
            install -m 0644 ${WORKDIR}/${BPN}.conf ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf
        fi
    else
        install -m 0755 ${S}/init_qcom_audio -D ${D}${sysconfdir}/init.d/init_qcom_audio
    fi

}

FILES:${PN} += "${systemd_unitdir}/system/*"
FILES:${PN}+="/etc/initscripts/start_audio_le"

