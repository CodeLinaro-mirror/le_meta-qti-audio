inherit autotools update-rc.d systemd

DESCRIPTION = "Installing audio init script"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"
PR = "r5"

DEPENDS_append_mdm9635 +="alsa-intf"

SRC_URI = "file://init_qcom_audio"
SRC_URI += "file://init_audio.service"
SRC_URI += "file://msm-audio-node.rules"
SRC_URI += "file://${BASEMACHINE}/"

do_compile[noexec] = "1"

S = "${WORKDIR}"

INITSCRIPT_NAME = "init_qcom_audio"
INITSCRIPT_PARAMS = "start 99 2 3 4 5 . stop 1 0 1 6 ."
INITSCRIPT_NAME_apq8009 = "init_qcom_audio"
INITSCRIPT_PARAMS_apq8009 = "start 38 2 3 4 5 . stop 1 0 1 6 ."

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${S}/msm-audio-node.rules -D ${D}${sysconfdir}/udev/rules.d/msm-audio-node.rules
        install -m 0644 ${S}/init_audio.service -D ${D}${systemd_unitdir}/system/init_audio.service
        install -d ${D}/${systemd_unitdir}/system/sysinit.target.wants
        install -d ${D}${sysconfdir}/initscripts
        install -m 0755 ${WORKDIR}/${MACHINE}/start_audio_le ${D}${sysconfdir}/initscripts
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

FILES_${PN} += "${systemd_unitdir}/system/*"
FILES_${PN}+="/etc/initscripts/start_audio_le"

