# Contributing to WarpNet Android

Thank you for your interest in contributing to WarpNet Android! This document provides guidelines for contributing to the project.

## Code of Conduct

Be respectful, inclusive, and considerate in all interactions.

## How to Contribute

### Reporting Bugs

When reporting bugs, include:

1. **Description**: Clear description of the issue
2. **Steps to Reproduce**: Detailed steps to reproduce the problem
3. **Expected Behavior**: What you expected to happen
4. **Actual Behavior**: What actually happened
5. **Environment**:
   - Android version
   - Device model
   - App version
   - Desktop node version
6. **Logs**: Relevant logcat output
7. **Screenshots**: If applicable

### Suggesting Features

Feature requests should include:

1. **Use Case**: Why this feature is needed
2. **Proposed Solution**: How it should work
3. **Alternatives**: Other solutions you've considered
4. **Impact**: Who benefits from this feature

### Pull Requests

#### Before Starting

1. Check existing issues and PRs
2. Discuss major changes in an issue first
3. Ensure your idea aligns with project goals

#### Development Process

1. **Fork the repository**

2. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**:
   - Follow the code style guide
   - Write meaningful commit messages
   - Add tests if applicable
   - Update documentation

4. **Test your changes**:
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

5. **Commit your changes**:
   ```bash
   git add .
   git commit -m "feat: Add your feature description"
   ```

6. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

7. **Create a Pull Request**:
   - Use a descriptive title
   - Reference related issues
   - Describe your changes
   - Include screenshots for UI changes

#### PR Requirements

- [ ] Code follows project style guidelines
- [ ] All tests pass
- [ ] New tests added for new functionality
- [ ] Documentation updated
- [ ] No unnecessary dependencies added
- [ ] Commits are clean and well-organized
- [ ] PR description is clear and complete

## Code Style

### Kotlin

Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
class UserProfile {
    private val userName: String = ""
    
    fun updateProfile(name: String) {
        // Implementation
    }
}

// Use descriptive names
val connectionTimeout = 30_000 // milliseconds

// Use trailing commas in multi-line collections
val addresses = listOf(
    "address1",
    "address2",
    "address3",
)
```

### Android

- Use AndroidX libraries
- Follow Material Design guidelines
- Use data binding or view binding
- Prefer Kotlin coroutines over callbacks

### Formatting

- Indentation: 4 spaces
- Line length: 120 characters maximum
- Use Android Studio's auto-formatter: `Ctrl+Alt+L`

### Documentation

```kotlin
/**
 * Connects to the desktop WarpNet node
 *
 * @param config Node configuration including peer ID and addresses
 * @return Result indicating success or failure
 */
suspend fun connect(config: NodeConfig): Result<Unit> {
    // Implementation
}
```

## Commit Messages

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Code style (formatting, missing semicolons, etc.)
- `refactor`: Code refactoring
- `perf`: Performance improvement
- `test`: Adding or updating tests
- `build`: Build system or dependencies
- `ci`: CI configuration
- `chore`: Other changes (maintenance)

### Examples

```
feat(qr): Add QR code scanning with camera permission handling

fix(connection): Resolve timeout issue with slow networks

docs(readme): Update build instructions for macOS

refactor(network): Extract API client into separate class

test(libp2p): Add unit tests for connection manager
```

## Testing

### Writing Tests

#### Unit Tests

```kotlin
@Test
fun `parseQRCode returns success for valid QR code`() {
    val qrContent = """
        {
            "peerId": "12D3KooW...",
            "addresses": ["addr1"],
            "sessionToken": "token123"
        }
    """.trimIndent()
    
    val result = QRCodeParser.parse(qrContent)
    
    assertTrue(result.isSuccess)
    assertEquals("12D3KooW...", result.getOrNull()?.peerId)
}
```

#### Instrumented Tests

```kotlin
@Test
fun testQRScanActivity() {
    val scenario = ActivityScenario.launch(QRScanActivity::class.java)
    
    onView(withId(R.id.barcode_scanner))
        .check(matches(isDisplayed()))
}
```

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Specific test class
./gradlew test --tests QRCodeParserTest

# With coverage
./gradlew testDebugUnitTest jacocoTestReport
```

## Documentation

### Code Comments

- Use comments to explain **why**, not **what**
- Keep comments up to date with code changes
- Use KDoc for public APIs

### README Updates

Update README.md when:
- Adding new features
- Changing setup process
- Modifying build instructions
- Updating requirements

### Documentation Files

Update relevant documentation:
- `DEVELOPMENT.md`: Development setup and workflows
- `LIBP2P_INTEGRATION.md`: libp2p implementation details
- `DESKTOP_API_SPEC.md`: API changes

## Dependencies

### Adding Dependencies

1. **Justify the dependency**: Explain why it's needed
2. **Check license**: Ensure compatible license
3. **Verify maintenance**: Is it actively maintained?
4. **Consider size**: Impact on APK size
5. **Security**: Check for known vulnerabilities

### Updating Dependencies

- Keep dependencies up to date
- Test thoroughly after updates
- Document breaking changes

## Security

### Reporting Security Issues

**Do not** open public issues for security vulnerabilities.

Instead:
1. Email the maintainers directly
2. Provide detailed information
3. Allow time for a fix before disclosure

### Security Considerations

- Never commit secrets or tokens
- Use encrypted storage for sensitive data
- Validate all user input
- Follow Android security best practices

## Review Process

### What We Look For

1. **Code Quality**:
   - Clean, readable code
   - Proper error handling
   - Efficient algorithms

2. **Testing**:
   - Adequate test coverage
   - Tests pass consistently

3. **Documentation**:
   - Clear documentation
   - Updated when necessary

4. **Impact**:
   - Minimal breaking changes
   - Backward compatibility when possible

### Review Timeline

- Initial review: Within 1 week
- Follow-up reviews: Within 3 days
- Merge: When approved by maintainers

### Addressing Feedback

- Respond to all comments
- Make requested changes
- Ask questions if unclear
- Push updates to the same PR

## Release Process

Releases follow [Semantic Versioning](https://semver.org/):

- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes

### Version Bumping

Update version in `app/build.gradle`:

```gradle
android {
    defaultConfig {
        versionCode 2
        versionName "1.1.0"
    }
}
```

## Getting Help

- **Documentation**: Check existing docs
- **Issues**: Search existing issues
- **Discussions**: Use GitHub Discussions
- **Contact**: Reach out to maintainers

## Recognition

Contributors are recognized in:
- Release notes
- CONTRIBUTORS.md file
- Git commit history

Thank you for contributing to WarpNet Android!
