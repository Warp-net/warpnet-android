# Dependency Vendoring Guide

This document explains how to vendor (locally cache) all Gradle dependencies for offline builds.

## What is Dependency Vendoring?

Dependency vendoring is the process of downloading all external dependencies and storing them locally in your project repository. This allows you to:

- Build the project offline
- Ensure dependency availability even if remote repositories are unavailable
- Speed up builds by eliminating network requests
- Lock dependencies to specific versions for reproducibility

## How to Vendor Dependencies

### Option 1: Vendor All Dependencies (Recommended)

Run the following command to vendor dependencies from all modules:

```bash
./gradlew vendorAllDependencies
```

This will execute the `vendorDependencies` task for each module (android, common, services) and copy all dependencies to the `repo` directory at the project root.

### Option 2: Vendor Dependencies Per Module

You can also vendor dependencies for individual modules:

```bash
# Vendor android module dependencies
./gradlew :android:vendorDependencies

# Vendor common module dependencies
./gradlew :common:vendorDependencies

# Vendor services module dependencies
./gradlew :services:vendorDependencies
```

## Where Are Dependencies Stored?

All vendored dependencies are stored in the `repo` directory at the project root:

```
warpnet-android/
├── repo/                    # Vendored dependencies stored here
│   ├── library1.jar
│   ├── library2.jar
│   └── ...
├── android/
├── common/
├── services/
└── settings.gradle.kts
```

## How Vendored Dependencies Are Used

The `settings.gradle.kts` file is already configured to check the `repo` directory first before checking remote repositories:

```kotlin
repositories {
    maven {
        url = uri("${settingsDir}/repo")  // Checked first
    }
    google()
    mavenCentral()
    // ... other repositories
}
```

This means:
1. Gradle will first look for dependencies in the `repo` directory
2. If not found, it will fall back to remote repositories (Google, Maven Central, etc.)
3. Once vendored, builds will use the local copies, making them faster and enabling offline builds

## Updating Vendored Dependencies

When you update dependencies in your `build.gradle.kts` files:

1. Run `./gradlew vendorAllDependencies` again to download the new versions
2. The new JARs will be copied to the `repo` directory
3. Duplicates are automatically handled (newer files replace older ones)

## Committing Vendored Dependencies

**Decision: Should you commit the `repo` directory?**

### Pros of Committing
- ✅ Enables truly offline builds for all team members
- ✅ Guarantees dependency availability
- ✅ No dependency on external repositories

### Cons of Committing
- ❌ Increases repository size significantly
- ❌ Binary files in version control
- ❌ Large commits when updating dependencies

### Recommendation

For most projects, **DO NOT commit the `repo` directory**. Instead:

1. Add `/repo` to `.gitignore` (if not already there)
2. Document the `./gradlew vendorAllDependencies` command
3. Run vendoring as needed for offline work

The `.gitignore` file should include:
```
repo/
```

## Technical Details

### Task Implementation

Each module's `vendorDependencies` task:

1. **Waits for plugin configuration**: Uses `afterEvaluate` to ensure all plugins have created their configurations
2. **Finds the runtime configuration**: Looks for `releaseRuntimeClasspath`, `debugRuntimeClasspath`, or `jvmRuntimeClasspath` (in that order)
3. **Copies JARs**: Copies all resolved dependencies to the `repo` directory
4. **Handles duplicates**: Uses `DuplicatesStrategy.EXCLUDE` to skip files that already exist

### Configuration Names

The task uses different configuration names depending on the module type:

- **Android modules**: `releaseRuntimeClasspath` (preferred) or `debugRuntimeClasspath`
- **KMP modules**: `releaseRuntimeClasspath`, `debugRuntimeClasspath`, or `jvmRuntimeClasspath`

### Limitations

1. **Flat structure**: Dependencies are stored as a flat list of JARs, not in Maven's hierarchical structure
2. **No metadata**: POM files and other metadata are not copied, only the JAR files
3. **Runtime classpath only**: The task copies dependencies from the runtime classpath configuration, which includes both compile-time and runtime dependencies along with their transitive dependencies. However, it doesn't vendor build-time-only dependencies like annotation processors or linters.
4. **No transitive resolution metadata**: The task copies resolved JAR files but doesn't maintain POM files that describe dependency relationships

For a more sophisticated vendoring solution with proper Maven repository structure, consider using:
- Gradle's `maven-publish` plugin
- JFrog Artifactory or Sonatype Nexus for local repository management
- Gradle's dependency locking feature

## Troubleshooting

### "No runtime configuration found" warning

If you see this warning, it means the module doesn't have any of the expected runtime configurations. This is normal for modules that don't produce runtime artifacts (e.g., pure Gradle plugin modules).

### Task not found

Make sure you're running the task after the build scripts have been evaluated. The task is created in an `afterEvaluate` block, so it won't be available if there are syntax errors in the build files.

### Dependencies not being used from repo

Check that:
1. The `repo` directory contains JAR files
2. `settings.gradle.kts` lists the `repo` directory first in the repositories block
3. You're running a clean build: `./gradlew clean build`

## Example Usage

```bash
# 1. Download and vendor all dependencies
./gradlew vendorAllDependencies

# 2. Verify the repo directory was created
ls -lh repo/

# 3. Try an offline build
./gradlew --offline build

# 4. If offline build fails, you may need to vendor more configurations
# or handle build-time dependencies separately
```

## See Also

- [Gradle Dependency Management](https://docs.gradle.org/current/userguide/dependency_management.html)
- [Gradle Offline Mode](https://docs.gradle.org/current/userguide/dependency_management.html#sub:cache_offline)
- [Gradle Dependency Locking](https://docs.gradle.org/current/userguide/dependency_locking.html)
