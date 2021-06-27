**FeedsClient v1.1.2**

**fix**

- Fail to initial log4j2 due to lack mutli-release flag in manifest (Issue #2)

**FeedsClient v1.1.1**

**fix**

- Duplicate items attached on different feed

**add**

- User-agent & follow redirection support
- For changing user-agent, add agent parameter on config.yml
- Simple start script
- Log4j2 support with log4j2.properties setting

**change**

- Disable unnecessary Upload artifact CI workflow

**FeedsClient v1.0.1**

**fix**

- issue #1 by disable minimizeJar & shadedArtifactAttached of maven-shade-plugin
- send mail with empty item content of feeds fix: reverse time comparation logic

**add**

- multiple time format support

**FeedsClient v1.0.0**

- Initial release
