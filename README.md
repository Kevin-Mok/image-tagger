# Image Tagger

A JavaFX desktop application for managing and organizing image collections through a tag-based system, featuring batch operations, automatic filename generation, and Imgur integration.

![Java](https://img.shields.io/badge/Java-8+-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-8-orange)
![Imgur](https://img.shields.io/badge/Imgur-API-green)

## Overview

A GUI application that helps organize large image collections by applying tags to images. Tags are reflected in filenames (as `@tagname` suffixes), enabling easy searching and filtering. Supports batch operations, name history tracking, and direct Imgur uploads.

## Features

### Core Functionality
- **Tag Management** - Add, remove, and filter by tags
- **Batch Operations** - Apply tags to multiple images or entire directories
- **Auto-Renaming** - Filenames automatically include applied tags as `@tag` suffixes
- **Name History** - Track all previous filenames with timestamps, revert with one click
- **Directory Browser** - Tree view navigation with image preview

### Advanced Features
- **Multi-Select** - Shift+click to select multiple images for batch tagging
- **Directory Tagging** - Apply tags to all images in a directory and subdirectories
- **Tag Filtering** - Filter image view by selected tags
- **Imgur Upload** - Direct upload to Imgur with API integration
- **Persistence** - Automatic save/restore of all data between sessions

## Tech Stack

| Component | Technology |
|-----------|------------|
| **Language** | Java 8+ |
| **GUI Framework** | JavaFX 8 |
| **HTTP Client** | Retrofit 2.2, OkHttp 3.0 |
| **JSON** | Gson 2.2 |
| **Testing** | JUnit 5 |
| **External API** | Imgur |

## Project Structure

```
image-tagger/
├── src/
│   ├── fx/                       # JavaFX GUI components
│   │   ├── Main.java             # Application entry point
│   │   ├── Controller.java       # Main UI controller
│   │   ├── PopUp.java            # Dialog windows
│   │   ├── layout.fxml           # JavaFX scene layout
│   │   ├── imgur/                # Imgur API integration
│   │   │   ├── ImgurAPI.java
│   │   │   └── ImageResponse.java
│   │   └── resources/icons/      # UI icons
│   └── main/                     # Core business logic
│       ├── Image.java            # Image entity
│       ├── ImageTagManager.java  # Singleton manager
│       ├── TagManager.java       # Per-image tag management
│       ├── Tag.java              # Tag entity
│       ├── DirectoryManager.java # File system operations
│       └── LogUtility.java       # Logging
├── test/                         # JUnit test suite
├── lib/                          # JAR dependencies
├── help.txt                      # User documentation
└── UML2.pdf                      # Architecture diagram
```

## Installation

### Prerequisites
- Java 8+ with JavaFX
- Bash shell (for build script)

### Build and Run

```bash
# Clone repository
git clone git@github.com:Kevin-Mok/image-tagger.git
cd image-tagger

# Compile and run
./src/compile_project.sh
```

### Manual Build

```bash
# Find all Java files
find src -name "*.java" > sources.txt

# Compile with dependencies
javac -cp "lib/*" @sources.txt -d out/class_files

# Run
java -cp "out/class_files:lib/*" fx.Main
```

## Usage

### Basic Workflow

1. **Select Directory** - Click "Choose Directory" to load images
2. **Browse Images** - Click images in tree view to preview
3. **Add Tags** - Type tag name and click "Add Tag" (or select existing)
4. **Apply to Image** - Select image(s) and tag(s), click "Apply"
5. **Batch Operations** - Shift+click multiple images, or click directory to select all

### Keyboard/Mouse Shortcuts

| Action | Control |
|--------|---------|
| Multi-select images | Shift+Click |
| Apply to directory | Click directory name |
| Filter by tag | Select tag, click "Filter" |
| Upload to Imgur | Press "i" or click Upload button |
| Open in file explorer | Click folder icon |

### Tag Format in Filenames

```
original-photo.jpg        →  original-photo @nature @sunset.jpg
```

## Data Persistence

- **Image data**: Serialized to `path_to_images.ser`
- **Last directory**: Saved to `last_dir.txt`
- **Tag associations**: Embedded in serialized image objects
- **Automatic cleanup**: Removes references to deleted files on load

## Why This Project is Interesting

### Technical Highlights

1. **Desktop Application Development**
   - Complete JavaFX GUI with FXML layouts
   - MVC architecture with controller separation
   - Event-driven programming with listeners

2. **Data Persistence Design**
   - Java serialization for complex object graphs
   - Automatic orphan cleanup
   - Session state restoration

3. **API Integration**
   - Retrofit HTTP client for Imgur API
   - Async upload handling
   - Response parsing with Gson

4. **Software Engineering Practices**
   - Singleton pattern for global state
   - Comprehensive unit tests with JUnit 5
   - UML documentation

### Skills Demonstrated

- **Java Development**: OOP, collections, I/O, serialization
- **GUI Programming**: JavaFX, FXML, event handling
- **API Integration**: REST clients, JSON parsing
- **Testing**: Unit testing, test fixtures
- **Architecture**: MVC pattern, singleton pattern

## Author

[Kevin Mok](https://github.com/Kevin-Mok)
