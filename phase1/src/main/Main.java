package main;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File imageFile = new File("/h/u3/c7/05/mokkar/Downloads/test.jpg");
        Picture i = new Picture(imageFile, "test");
        i.rename("@test");
    }
}
