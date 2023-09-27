package com.noname.enums;

public enum ImageType {
        USER("/home/ec2-user/upload/user"),
        PRODUCT("/home/ec2-user/upload/product");

        private final String uploadPath;

        private ImageType(String uploadPath) {
            this.uploadPath = uploadPath;
        }

        public String getUploadPath() {
            return uploadPath;
        }


}
