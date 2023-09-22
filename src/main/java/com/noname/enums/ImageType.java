package com.noname.enums;

public enum ImageType {
        USER("/Users/buenhjcho/WebDevelop/02.personal/01.springjpa/workspace/upload/user"),
        PRODUCT("/Users/buenhjcho/WebDevelop/02.personal/01.springjpa/workspace/upload");

        private final String uploadPath;

        private ImageType(String uploadPath) {
            this.uploadPath = uploadPath;
        }

        public String getUploadPath() {
            return uploadPath;
        }


}
