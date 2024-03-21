include $(TOPDIR)/rules.mk
include $(INCLUDE_DIR)/kernel.mk

PKG_NAME:=audio-kernel
PKG_VERSION:=1.0
PKG_RELEASE:=1
PKG_LICENSE:=GPL-2.0
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/audio-kernel

include $(INCLUDE_DIR)/package.mk

define KernelPackage/audio-kernel
	SECTION:=Kernel Modules
	CATEGORY:=External Kernel Modules
	TITLE:=AUDIO-KERNEL
endef

define Package/audio-kernel/Description
	AUDIO-KERNEL
endef

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

MAKE_OPTS:= \
    M="$(PKG_BUILD_DIR)" \
    TARGET_SUPPORT="$(KERNEL_PLATFORM_TARGET)"

define Build/Compile
	$(MAKE) -C "$(LINUX_DIR)" \
	$(MAKE_OPTS) \
	modules
endef

define Build/InstallDev
	$(INSTALL_DIR) $(STAGING_DIR)
	mkdir -p $(1)/usr/include/audio-kernel/
	$(CP) $(PKG_BUILD_DIR)/include/* $(1)/usr/include/audio-kernel/
endef

define KernelPackage/audio-kernel/install
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/machine_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/platform_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/codecs/mbhc_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/codecs/stub_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/codecs/wcd9xxx_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/codecs/wcd_core_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/codecs/wsa881x_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/asoc/codecs/wcd934x/wcd934x_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/dsp/adsp_loader_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/dsp/audio_prm_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/dsp/audpkt_ion_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/dsp/q6_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/dsp/q6_notifier_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/dsp/q6_pdr_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/dsp/spf_core_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/soc/pinctrl_wcd_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/soc/snd_event_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/soc/swr_ctrl_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/soc/swr_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/ipc/audio_pkt_dlkm.ko
	$(SIGN_KEY) $(PKG_BUILD_DIR)/ipc/gpr_dlkm.ko
	$(INSTALL_DIR) $(1)/lib/modules/audio/asoc
	$(INSTALL_DIR) $(1)/lib/modules/audio/asoc/codecs/wcd934x/
	$(INSTALL_DIR) $(1)/lib/modules/audio/asoc/codecs/
	$(INSTALL_DIR) $(1)/lib/modules/audio/dsp/
	$(INSTALL_DIR) $(1)/lib/modules/audio/soc/
	$(INSTALL_DIR) $(1)/lib/modules/audio/ipc/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/asoc/*.ko $(1)/lib/modules/audio/asoc/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/asoc/codecs/wcd934x/*.ko $(1)/lib/modules/audio/asoc/codecs/wcd934x/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/asoc/codecs/*.ko $(1)/lib/modules/audio/asoc/codecs/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/dsp/*.ko $(1)/lib/modules/audio/dsp/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/soc/*.ko $(1)/lib/modules/audio/soc/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/ipc/*.ko $(1)/lib/modules/audio/ipc/
	#install audio-kernel ko's in /bin/targets/$(TARGET_VARIANT)/modules to be shown in owrt CRM APPS
	$(INSTALL_DIR) $(TOPDIR)/bin/targets/$(TARGET_VARIANT)/modules
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.pkgdir/kmod-audio-kernel/lib/modules/audio/asoc/*.ko $(TOPDIR)/bin/targets/$(TARGET_VARIANT)/modules/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.pkgdir/kmod-audio-kernel/lib/modules/audio/asoc/codecs/wcd934x/*.ko $(TOPDIR)/bin/targets/$(TARGET_VARIANT)/modules/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.pkgdir/kmod-audio-kernel/lib/modules/audio/asoc/codecs/*.ko $(TOPDIR)/bin/targets/$(TARGET_VARIANT)/modules/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.pkgdir/kmod-audio-kernel/lib/modules/audio/dsp/*.ko $(TOPDIR)/bin/targets/$(TARGET_VARIANT)/modules/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.pkgdir/kmod-audio-kernel/lib/modules/audio/soc/*.ko $(TOPDIR)/bin/targets/$(TARGET_VARIANT)/modules/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.pkgdir/kmod-audio-kernel/lib/modules/audio/ipc/*.ko $(TOPDIR)/bin/targets/$(TARGET_VARIANT)/modules/
endef

$(eval $(call KernelPackage,audio-kernel))
