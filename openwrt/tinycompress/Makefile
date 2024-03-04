include $(TOPDIR)/rules.mk

PKG_NAME:=tinycompress
PKG_RELEASE:=1
PKG_LICENSE:=BSD-3-Clause
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/tinycompress

include $(INCLUDE_DIR)/package.mk

define Package/tinycompress
  SECTION:=AUDIO
  CATEGORY:=AUDIO
  MAINTAINER:=QTI MM Audio
  TITLE:=TINYCOMPRESS
  DEPENDS:= +libcutils +glib2
endef

define Package/tinycompress/description
	TinyCompress Library
endef

CONFIGURE_ARGS += \
                  --with-openwrt \
                  --with-glib

TARGET_CFLAGS += \
                  -DFEATURE_IPQ_OPENWRT

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

define Build/InstallDev
  $(INSTALL_DIR) $(STAGING_DIR)
	mkdir -p $(1)/usr/include
	mkdir -p $(1)/usr/include/tinycompress
	mkdir -p $(1)/usr/lib
	$(CP) $(PKG_BUILD_DIR)/*.h $(1)/usr/include/tinycompress
	$(CP) $(PKG_BUILD_DIR)/include/tinycompress/*.h $(1)/usr/include/tinycompress
	$(CP) $(PKG_BUILD_DIR)/.libs/* $(1)/usr/lib/
endef

define Package/tinycompress/install
	$(INSTALL_DIR) $(1)/usr/lib/
	$(CP) $(PKG_BUILD_DIR)/.libs/*.so* $(1)/usr/lib/
endef

$(eval $(call BuildPackage,tinycompress))
