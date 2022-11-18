include $(TOPDIR)/rules.mk

PKG_NAME:=init-audio
PKG_RELEASE:=1

include $(INCLUDE_DIR)/package.mk

define Package/init-audio
  SECTION:=AUDIO
  CATEGORY:=AUDIO
  MAINTAINER:=QTI MM Audio
  TITLE:=INIT-AUDIO
endef

define Package/init-audio/description
	INIT-AUDIO
endef

define Build/Compile
    @true
endef

define Package/init-audio/install
	$(INSTALL_DIR) $(1)/etc/init.d
	$(INSTALL_DIR) $(1)/etc/initscripts/
	$(INSTALL_DIR) $(1)/etc/udev/rules.d/
	$(INSTALL_BIN) ./files/init_audio.init $(1)/etc/init.d/
	$(INSTALL_BIN) ./files/init_qcom_audio $(1)/etc/init.d/
	$(INSTALL_BIN) ./files/start_audio_le  $(1)/etc/initscripts/
	$(CP) ./files/msm-audio-node.rules  $(1)/etc/udev/rules.d/
endef

$(eval $(call BuildPackage,init-audio))
