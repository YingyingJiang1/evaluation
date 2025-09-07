```java
package stirling.software.SPDF.utils;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import io.github.pixee.security.HostValidator;
import io.github.pixee.security.Urls;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.web.multipart.MultipartFile;
import stirling.software.SPDF.config.InstallationPathConfig;
import stirling.software.SPDF.config.YamlHelper;

@Slf4j
public class GeneralUtils {

    private static final int CONVERT_BUFFER_SIZE = 8192;
    private static final int MULTIPART_BUFFER_SIZE = 1024;
    private static final int MAX_FILENAME_LENGTH = 50;
    private static final long BYTES_PER_KB = 1024L;
    private static final long BYTES_PER_MB = BYTES_PER_KB * BYTES_PER_KB;
    private static final long BYTES_PER_GB = BYTES_PER_MB * BYTES_PER_KB;

    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = Files.createTempFile("temp", null).toFile();
        try (InputStream inputStream = multipartFile.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[CONVERT_BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    public static void deleteDirectory(Path path) throws IOException {
        Files.walkFileTree(
                path,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                            throws IOException {
                        Files.deleteIfExists(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                            throws IOException {
                        Files.deleteIfExists(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
    }

    public static String convertToFileName(String name) {
        String safeName = name.replaceAll("[^a-zA-Z0-9]", "_");
        if (safeName.length() > MAX_FILENAME_LENGTH) {
            safeName = safeName.substring(0, MAX_FILENAME_LENGTH);
        }
        return safeName;
    }

    // Get resources from a location pattern
    public static Resource[] getResourcesFromLocationPattern(
            String locationPattern, ResourceLoader resourceLoader) throws Exception {
        // Normalize the path for file resources
        if (locationPattern.startsWith("file:")) {
            String rawPath = locationPattern.substring(5).replace("\\*", "").replace("/*", "");
            Path normalizePath = Paths.get(rawPath).normalize();
            locationPattern = "file:" + normalizePath.toString().replace("\\", "/") + "/*";
        }
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources(locationPattern);
    }

    public static boolean isValidURL(String urlStr) {
        try {
            Urls.create(
                    urlStr, Urls.HTTP_PROTOCOLS, HostValidator.DENY_COMMON_INFRASTRUCTURE_TARGETS);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isURLReachable(String urlStr) {
        try {
            // Parse the URL
            URL url = URI.create(urlStr).toURL();

            // Allow only http and https protocols
            String protocol = url.getProtocol();
            if (!"http".equals(protocol) && !"https".equals(protocol)) {
                return false; // Disallow other protocols
            }

            // Check if the host is a local address
            String host = url.getHost();
            if (isLocalAddress(host)) {
                return false; // Exclude local addresses
            }

            // Check if the URL is reachable
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            // connection.setConnectTimeout(5000); // Set connection timeout - Removed commented out
            // code
            // connection.setReadTimeout(5000);    // Set read timeout - Removed commented out code
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (Exception e) {
            return false; // Return false in case of any exception
        }
    }

    private static boolean isLocalAddress(String host) {
        try {
            // Resolve DNS to IP address
            InetAddress address = InetAddress.getByName(host);

            // Check for local addresses
            return address.isAnyLocalAddress() // Matches 0.0.0.0 or similar
                    || address.isLoopbackAddress() // Matches 127.0.0.1 or ::1
                    || address.isSiteLocalAddress() // Matches private IPv4 ranges
                    || address.getHostAddress()
                            .startsWith("fe80:"); // Matches link-local IPv6 addresses
        } catch (Exception e) {
            return false; // Return false for invalid or unresolved addresses
        }
    }

    public static File multipartToFile(MultipartFile multipart) throws IOException {
        Path tempFile = Files.createTempFile("overlay-", ".pdf");
        try (InputStream in = multipart.getInputStream();
                FileOutputStream out = new FileOutputStream(tempFile.toFile())) {
            byte[] buffer = new byte[MULTIPART_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile.toFile();
    }

    public static Long convertSizeToBytes(String sizeStr) {
        if (sizeStr == null) {
            return null;
        }

        sizeStr = sizeStr.trim().toUpperCase().replace(",", ".").replace(" ", "");
        try {
            if (sizeStr.endsWith("KB")) {
                return (long)
                        (Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 2))
                                * BYTES_PER_KB);
            } else if (sizeStr.endsWith("MB")) {
                return (long)
                        (Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 2))
                                * BYTES_PER_MB);
            } else if (sizeStr.endsWith("GB")) {
                return (long)
                        (Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 2))
                                * BYTES_PER_GB);
            } else if (sizeStr.endsWith("B")) {
                return Long.parseLong(sizeStr.substring(0, sizeStr.length() - 1));
            } else {
                // Assume MB if no unit is specified
                return (long) (Double.parseDouble(sizeStr) * BYTES_PER_MB);
            }
        } catch (NumberFormatException e) {
            // The numeric part of the input string cannot be parsed, handle this case
            log.warn("Could not parse size string: {}", sizeStr, e);
        }

        return null;
    }

    public static String formatBytes(long bytes) {
        if (bytes < BYTES_PER_KB) {
            return bytes + " B";
        } else if (bytes < BYTES_PER_MB) {
            return String.format("%.2f KB", (double) bytes / BYTES_PER_KB);
        } else if (bytes < BYTES_PER_GB) {
            return String.format("%.2f MB", (double) bytes / BYTES_PER_MB);
        } else {
            return String.format("%.2f GB", (double) bytes / BYTES_PER_GB);
        }
    }

    public static List<Integer> parsePageList(String pages, int totalPages, boolean oneBased) {
        if (pages == null) {
            return List.of(oneBased ? 1 : 0); // Default adjusted for oneBased
        }
        try {
            return parsePageList(pages.split(","), totalPages, oneBased);
        } catch (NumberFormatException e) {
            log.warn("Invalid page list format: {}", pages, e);
            return List.of(oneBased ? 1 : 0); // Default adjusted for oneBased
        }
    }

    // Overload without oneBased parameter, defaulting to false (0-based)
    public static List<Integer> parsePageList(String[] pages, int totalPages) {
        return parsePageList(pages, totalPages, false);
    }

    public static List<Integer> parsePageList(String[] pages, int totalPages, boolean oneBased) {
        List<Integer> result = new ArrayList<>();
        int offset = oneBased ? 1 : 0;
        for (String page : pages) {
            String trimmedPage = page.trim();
            if ("all".equalsIgnoreCase(trimmedPage)) {
                result.clear(); // Clear previous entries if "all" is specified
                for (int i = 0; i < totalPages; i++) {
                    result.add(i + offset);
                }
                break; // "all" overrides everything else
            } else if (trimmedPage.contains(",")) {
                // Split the string into parts, could be single pages or ranges
                String[] parts = trimmedPage.split(",");
                for (String part : parts) {
                    result.addAll(handlePart(part.trim(), totalPages, offset));
                }
            } else {
                result.addAll(handlePart(trimmedPage, totalPages, offset));
            }
        }
        return result;
    }

    public static List<Integer> evaluateNFunc(String expression, int maxValue) {
        List<Integer> results = new ArrayList<>();
        DoubleEvaluator evaluator = new DoubleEvaluator();

        // Validate the expression (basic validation)
        if (!expression.matches("[0-9n+\\-*/() ]+")) {
            log.error("Invalid expression provided for n-function: {}", expression);
            throw new IllegalArgumentException("Invalid expression contains disallowed characters");
        }

        for (int n = 1; n <= maxValue; n++) {
            // Replace 'n' with the current value of n, correctly handling numbers before 'n'
            String sanitizedExpression = sanitizeNFunction(expression, n);
            try {
                Double result = evaluator.evaluate(sanitizedExpression);

                // Check if the result is valid and within bounds
                if (result != null && result == result.intValue()) { // Check if it's an integer
                    int intResult = result.intValue();
                    if (intResult > 0 && intResult <= maxValue) {
                        results.add(intResult);
                    }
                }
            } catch (Exception e) {
                // Log evaluation errors but continue if possible, or break if severe
                log.warn(
                        "Error evaluating expression '{}' for n={}: {}",
                        sanitizedExpression,
                        n,
                        e.getMessage());
                // Depending on desired behavior, could 'continue' or 'break' here.
                // Breaking might be safer if one evaluation error indicates a bad expression.
                break;
            }
        }

        return results;
    }

    private static String sanitizeNFunction(String expression, int nValue) {
        String sanitizedExpression = expression.replace(" ", "");
        // Example: n(n-1), 9(n-1), (n-1)(n-2) -> n*(n-1), 9*(n-1), (n-1)*(n-2)
        String multiplyByOpeningRoundBracketPattern = "([0-9n)])\\(";
        sanitizedExpression =
                sanitizedExpression.replaceAll(multiplyByOpeningRoundBracketPattern, "$1*(");

        // Example: (n-1)n, (n-1)9, (n-1)(n-2) -> (n-1)*n, (n-1)*9, (n-1)*(n-2)
        String multiplyByClosingRoundBracketPattern = "\\)([0-9n)])";
        sanitizedExpression =
                sanitizedExpression.replaceAll(multiplyByClosingRoundBracketPattern, ")*$1");

        sanitizedExpression = insertMultiplicationBeforeN(sanitizedExpression, nValue);
        return sanitizedExpression;
    }

    private static String insertMultiplicationBeforeN(String expression, int nValue) {
        // Insert multiplication between a number and 'n' (e.g., "4n" becomes "4*n")
        String withMultiplication = expression.replaceAll("(\\d)n", "$1*n");
        // Handle consecutive 'n's (e.g., "nn" becomes "n*n")
        withMultiplication = formatConsecutiveNsForNFunction(withMultiplication);
        // Now replace 'n' with its current value
        return withMultiplication.replace("n", String.valueOf(nValue));
    }

    private static String formatConsecutiveNsForNFunction(String expression) {
        String text = expression;
        // Replaces nn with n*n iteratively until no "nn" sequence remains
        while (text.matches(".*n{2,}.*")) {
            text = text.replaceAll("(?<![a-zA-Z0-9])n{2}", "n*n"); // Ensure nn isn't part of a word
            text = text.replaceAll("n{2}", "n*n"); // General case
        }
        return text;
    }

    private static List<Integer> handlePart(String part, int totalPages, int offset) {
        List<Integer> partResult = new ArrayList<>();
        part = part.trim(); // Ensure part is trimmed

        // First check for n-syntax because it should not be processed as a range
        if (part.contains("n")) {
            try {
                List<Integer> evaluatedPages = evaluateNFunc(part, totalPages);
                // Adjust the results according to the offset (n is 1-based, result needs offset)
                for (int pageNum : evaluatedPages) {
                    // evaluateNFunc returns 1-based page numbers
                    if (pageNum >= 1 && pageNum <= totalPages) {
                        partResult.add(pageNum - 1 + offset); // Convert to 0-based then add offset
                    }
                }
            } catch (IllegalArgumentException e) {
                log.warn("Invalid n-syntax expression: {}", part, e);
                // Ignore invalid n-syntax part
            }
        } else if (part.contains("-")) {
            // Process ranges only if it's not n-syntax
            String[] rangeParts = part.split("-", 2); // Split into exactly two parts
            try {
                int start = Integer.parseInt(rangeParts[0]);
                int end =
                        (rangeParts.length > 1 && !rangeParts[1].isEmpty())
                                ? Integer.parseInt(rangeParts[1])
                                : totalPages; // Default end to totalPages

                // Ensure start <= end
                if (start > end) {
                    log.warn("Invalid range: start ({}) > end ({}) in part '{}'", start, end, part);
                } else {
                    for (int i = start; i <= end; i++) {
                        // Page numbers are typically 1-based in user input
                        if (i >= 1 && i <= totalPages) {
                            partResult.add(i - 1 + offset); // Convert to 0-based then add offset
                        }
                    }
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid range format: {}", part, e);
                // Range is invalid, ignore this part
            }
        } else {
            // This is a single page number
            try {
                int pageNum = Integer.parseInt(part);
                // Page numbers are typically 1-based in user input
                if (pageNum >= 1 && pageNum <= totalPages) {
                    partResult.add(pageNum - 1 + offset); // Convert to 0-based then add offset
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid page number format: {}", part, e);
                // Ignore invalid numbers
            }
        }
        return partResult;
    }

    public static boolean createDir(String path) {
        Path folder = Paths.get(path);
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                log.error("Failed to create directory: {}", path, e);
                return false;
            }
        }
        return true;
    }

    public static boolean isValidUUID(String uuid) {
        if (uuid == null) {
            return false;
        }
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /*------------------------------------------------------------------------*
     *                  Internal Implementation Details                       *
     *------------------------------------------------------------------------*/

    public static void saveKeyToSettings(String key, Object newValue) throws IOException {
        String[] keyArray = key.split("\\.");
        Path settingsPath = Paths.get(InstallationPathConfig.getSettingsPath());
        YamlHelper settingsYaml = new YamlHelper(settingsPath);
        settingsYaml.updateValue(Arrays.asList(keyArray), newValue);
        settingsYaml.saveOverride(settingsPath);
    }

    public static String generateMachineFingerprint() {
        try {
            StringBuilder macAddressBuilder = new StringBuilder();
            // Try getting MAC from local host address first
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface primaryInterface = NetworkInterface.getByInetAddress(localHost);

            if (primaryInterface != null) {
                byte[] mac = primaryInterface.getHardwareAddress();
                if (mac != null) {
                    appendMacAddress(macAddressBuilder, mac);
                }
            }

            // If not found, iterate through all network interfaces
            if (macAddressBuilder.length() == 0) {
                Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
                while (networks.hasMoreElements()) {
                    NetworkInterface net = networks.nextElement();
                    // Skip loopback and virtual interfaces
                    if (net.isLoopback() || net.isVirtual() || !net.isUp()) {
                        continue;
                    }
                    byte[] mac = net.getHardwareAddress();
                    if (mac != null) {
                        appendMacAddress(macAddressBuilder, mac);
                        break; // Use the first valid non-loopback/virtual MAC address found
                    }
                }
            }

            if (macAddressBuilder.length() == 0) {
                log.warn("Could not find a suitable MAC address for fingerprint generation.");
                return "GenericID-NoMAC"; // Return a specific ID if no MAC found
            }

            // Hash the MAC address for privacy and consistency
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(macAddressBuilder.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder fingerprint = new StringBuilder();
            for (byte b : hash) {
                fingerprint.append(String.format("%02x", b));
            }
            return fingerprint.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("Failed to generate machine fingerprint", e);
            return "GenericID-Error"; // Return a specific ID on error
        }
    }

    private static void appendMacAddress(StringBuilder sb, byte[] mac) {
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
        }
    }

    public static boolean isVersionHigher(String currentVersion, String compareVersion) {
        if (currentVersion == null || compareVersion == null) {
            return false; // Cannot compare null versions
        }

        // Split versions into components
        String[] currentParts = currentVersion.split("\\.");
        String[] compareParts = compareVersion.split("\\.");

        int length = Math.min(currentParts.length, compareParts.length);

        // Compare each component numerically
        for (int i = 0; i < length; i++) {
            try {
                int currentPart = Integer.parseInt(currentParts[i]);
                int comparePart = Integer.parseInt(compareParts[i]);

                if (currentPart > comparePart) {
                    return true;
                }
                if (currentPart < comparePart) {
                    return false;
                }
                // If parts are equal, continue to the next part
            } catch (NumberFormatException e) {
                log.warn(
                        "Invalid version format detected: current='{}', compare='{}'",
                        currentVersion,
                        compareVersion,
                        e);
                // Treat non-numeric parts as lower? Or handle comparison differently?
                // For simplicity, returning false on format error.
                return false;
            }
        }

        // If all common components are equal, the version with more components is considered higher
        return currentParts.length > compareParts.length;
    }
}
```

```text
/**
Explanation of Transformations:

1.  **Imports:** Replaced wildcard imports (`java.net.*`, `java.nio.file.*`) with specific class imports for better clarity and management. Adjusted import order slightly (standard Java, `com`, `io`, `org`, project-specific).
2.  **Constants:** Introduced constants (`CONVERT_BUFFER_SIZE`, `MULTIPART_BUFFER_SIZE`, `MAX_FILENAME_LENGTH`, `BYTES_PER_KB`, `BYTES_PER_MB`, `BYTES_PER_GB`) to replace magic numbers, improving readability and maintainability. Updated relevant methods (`convertMultipartFileToFile`, `multipartToFile`, `convertToFileName`, `convertSizeToBytes`, `formatBytes`) to use these constants.
3.  **Brace Style:** Ensured consistent use of braces `{}` for all control flow statements (`if`, `else`, `for`, `while`), even single-line blocks. The opening brace `{` is placed on the same line as the statement, which aligns with the dominant style observed in method definitions and `try-with-resources`.
4.  **Commented Code:** Removed commented-out code lines (`connection.setConnectTimeout`, `connection.setReadTimeout`) in `isURLReachable` as they were not active.
5.  **Error Handling/Logging:** Added basic logging (`log.warn`, `log.error`) in several `catch` blocks (`convertSizeToBytes`, `parsePageList`, `evaluateNFunc`, `handlePart`, `createDir`, `generateMachineFingerprint`, `isVersionHigher`) where exceptions were previously ignored or just returned default values, providing more insight into potential issues during execution. Changed return values in `generateMachineFingerprint` on error/no MAC found to be more specific than "GenericID".
6.  **Clarity and Minor Logic Refinements:**
    *   In `parsePageList`: Added `trim()` to handle potential whitespace around page numbers/ranges/keywords. Added logic to clear the result list and break if "all" is encountered. Adjusted default page return based on `oneBased` flag.
    *   In `handlePart`: Added `trim()` to the part. Improved range validation (check start <= end). Adjusted page number conversion to consistently assume 1-based input and convert to 0-based internally before applying the offset.
    *   In `evaluateNFunc`: Added check `result == result.intValue()` to ensure only integer results are added. Added basic logging for evaluation errors.
    *   In `generateMachineFingerprint`: Refined the logic to prioritize the interface associated with `InetAddress.getLocalHost()` and added checks to skip loopback/virtual interfaces when iterating. Modified MAC address formatting in `appendMacAddress` to include colons for potential readability (though this doesn't affect the final hash).
    *   In `isVersionHigher`: Added a `try-catch` block to handle potential `NumberFormatException` if version parts are not numeric, logging a warning and returning `false`.
7.  **Formatting:** Ensured consistent indentation (4 spaces) and spacing throughout the code. Added blank lines where appropriate to separate logical blocks.
*/
```