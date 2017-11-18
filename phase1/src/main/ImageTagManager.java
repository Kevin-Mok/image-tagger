package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class ImageTagManager {
    private static ImageTagManager instance = null;
    private HashMap<String, ArrayList<Image>> allTags;
    private HashMap<String, Image> allImages;

    private ImageTagManager() {
        // Exists only to defeat instantiation.
    }

    public static ImageTagManager getInstance() {
        if (instance == null) {
            instance = new ImageTagManager();
            instance.allTags = new HashMap<>();
            instance.allImages = new HashMap<>();
        }
        return instance;
    }

    public String[] getListOfTags() {
        Set<String> setOfTagString = allTags.keySet();
        String[] listOfTags = setOfTagString.toArray(new
                String[setOfTagString.size()]);
        Arrays.sort(listOfTags);
        return listOfTags;
    }

    public void removeImage(String path) {
        allImages.remove(path);
    }

    public Image getImage(String path) {
        return allImages.get(path);
    }

    public boolean containsImagePath(String path) {
        return allImages.containsKey(path);
    }


    public void addImage(Image image) {
        allImages.put(image.getPath().toString(), image);
        System.out.println(allImages);
    }

    public void rebuildTagList() {
        HashMap<String, ArrayList<Image>> rebuild = new HashMap<>();
        for (String keys : allImages.keySet()) {
            mapBuilder(allImages.get(keys), rebuild);
        }
        allTags = rebuild;
        System.out.println(allTags);
    }

    private void mapBuilder(Image image, HashMap<String, ArrayList<Image>>
            map) {
        for (String tagName : image.getTagManager().getTagNames()) {
            if (map.containsKey(tagName)) {
                map.get(tagName).add(image);
            } else {
                map.put(tagName, new ArrayList<>());
                map.get(tagName).add(image);
            }
        }
    }


    public void saveToFile() throws IOException {
        deleteUselessImageObjects();
        OutputStream file = new FileOutputStream("images.ser");
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        OutputStream file1 = new FileOutputStream("tags.ser");
        OutputStream buffer1 = new BufferedOutputStream(file1);
        ObjectOutput output1 = new ObjectOutputStream(buffer1);

        // serialize the Map
        output1.writeObject(allTags);
        output.writeObject(allImages);
        output.close();
        output1.close();
    }

    private void deleteUselessImageObjects() {
        HashMap<String, Image> rebuild = new HashMap<>();
        for (String keys : allImages.keySet()) {
            if (allImages.get(keys).getTagManager().getNameHistory().size()
                    != 1) {
                rebuild.put(keys, allImages.get(keys));
            }
        }
        allImages = rebuild;
    }

    public void readFromFile() throws IOException {

        InputStream file;
        try {
            file = new FileInputStream("images.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            InputStream file1 = new FileInputStream("tags.ser");
            InputStream buffer1 = new BufferedInputStream(file1);
            ObjectInput input1 = new ObjectInputStream(buffer1);
            Object a = null;
            Object b = null;
            try {
                a = input.readObject();
                b = input1.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Stopped");
            }
            allImages = (HashMap<String, Image>) a;
            allTags = (HashMap<String, ArrayList<Image>>) b;

            input.close();
            input1.close();

        } catch (FileNotFoundException e) {
            System.out.println("Ser files not found. They have been created");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(allImages);
        System.out.println(allTags);

    }

}


