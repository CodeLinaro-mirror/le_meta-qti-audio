# Rootfs creation.

IMAGE_INSTALL += "init-audio"

# Following packages support only 32-bit compilation.
# When 32-bit multilib is enabled, compile them with lib32- prefix.

IMAGE_INSTALL += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-audiohal', 'audiohal', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-tinyalsa', 'tinyalsa', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-tinycompress', 'tinycompress', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-encoders', 'encoders', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-qahw', 'qahw', d)}"
AUDIOMODULE_FLAG = "${@bb.utils.contains('DISTRO_FEATURES', 'audio-dlkm', 'True', 'False', d)}"
IMAGE_INSTALL += "${@oe.utils.conditional('AUDIOMODULE_FLAG', 'True', bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-audiodlkm', 'audiodlkm', d), '', d)}"

copy_edge_audio_rpm() {
    cp ${DEPLOY_DIR_RPM}/aarch64/init-audio-git-r5.aarch64.rpm ${IMAGE_ROOTFS}/packages/qcaudio/
    cp ${DEPLOY_DIR_RPM}/aarch64/tinyalsa-git-r1.aarch64.rpm ${IMAGE_ROOTFS}/packages/qcaudio/
    cp ${DEPLOY_DIR_RPM}/aarch64/tinycompress-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/qcaudio/
    cp ${DEPLOY_DIR_RPM}/aarch64/qahw-git-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/qcaudio/
    cp ${DEPLOY_DIR_RPM}/aarch64/encoders-1.0-r0.aarch64.rpm ${IMAGE_ROOTFS}/packages/qcaudio/
    cp ${DEPLOY_DIR_RPM}/sm8250/audiohal-1.0-r0.sm8250.rpm ${IMAGE_ROOTFS}/packages/qcaudio/
    cp ${DEPLOY_DIR_RPM}/sm8250/audiodlkm-git-r0.sm8250.rpm ${IMAGE_ROOTFS}/packages/qcaudio/
}

ROOTFS_POSTPROCESS_COMMAND += "copy_edge_audio_rpm;"
