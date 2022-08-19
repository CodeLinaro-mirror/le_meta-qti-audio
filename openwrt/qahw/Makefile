include $(TOPDIR)/rules.mk

PKG_NAME:=qahw
PKG_RELEASE:=1
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/audio-hal/primary-hal/qahw/

include $(INCLUDE_DIR)/package.mk

define Package/qahw
  SECTION:=AUDIO
  CATEGORY:=AUDIO
  MAINTAINER:=QTI MM Audio
  TITLE:=QAHW
  DEPENDS:= +libcutils +glib2 +liblog +media-headers +audio-utils +libhardware +core-include
endef

define Package/qahw/description
	QAHW Package
endef

CONFIGURE_ARGS += \
                  --with-openwrt \
                  --with-glib

TARGET_CFLAGS += \
                   -DFEATURE_IPQ_OPENWRT \

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

define Build/InstallDev
	$(INSTALL_DIR) $(STAGING_DIR)
	mkdir -p $(1)/usr/include
	mkdir -p $(1)/usr/include/qahw
	mkdir -p $(1)/usr/lib
	$(CP) $(PKG_BUILD_DIR)/inc/*.h $(1)/usr/include/qahw
	$(CP) $(PKG_BUILD_DIR)/.libs/* $(1)/usr/lib/
endef

define Package/qahw/install
	$(INSTALL_DIR) $(1)/usr/lib/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/* $(1)/usr/lib/
endef

$(eval $(call BuildPackage,qahw))
