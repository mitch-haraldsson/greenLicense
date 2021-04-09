# greenLicense

Green license is a tool to license your software against misuse. Like any other piece of license software it is not impervious to circumvention, but it gives the attacker a good amount of work until they acquire ONE free version.

# How does it work?
## The manager

###  Creating a key pair 
- The private key is used to issue a license to your software.
- The public key is distributed with your software. Hardcoded into the program.
    
### Manage software
- Create a software and assign a key to it. 
- Enter any amount of information you want to have in your software. It is always composed of an ID (the feature) and a value. For example how long the license is valid or what features are enabled.
- you can set a default value for every feature
- choose your license version 
    - Version 1 lets you simply issue a license for your software 
    - Version 2 also lets you bind your license to a single machine identified by mac addresses. With the "ID Generator" a license ID is generated which reflects the available mac addresses of a system. This ID is coded into the license file. At least one MAC address has to match for the license to be recognized as valid.
    
### Issuing a license
A license can be issued with the default values for a software or with custom values per license.
    
### CLI EXAMPLES
#### Create a key pair
```
java -jar manager.jar keypair create --name "MyCoolSoftware Version 1.0" --size 4096
```
This creates a key with the human-readable name "MyCoolSoftware Version 1.0" and a key size of 4096. As a result of this operation you receive an ID. Let's assume it is `3a6b01bd-5f11-4cbe-87c4-c527895728a9`. This ID is used to identify your key. Save this ID for the next step.

Keep in mind, that the key is here to protect your software. This means, if you have "Software A", licensed by "Key A" and "Software B" also licensed by "Key A", then "Software B" will recognize a license for "Software A" also as valid. The only thing preventing a misuse here would be any feature you might evaluate.

So, it is good practice creating a new key pair for every software, or version you don't want to work with an older license.

#### Create a software
```
java -jar manager.jar software create --key "3a6b01bd-5f11-4cbe-87c4-c527895728a9" --license 2 --name "MyCoolSoftware" --version "1.0"
```
This creates a software for license version "2" (the one that needs a system ID) with human-readable name "MyCoolSoftware" in Version "1.0". Bound to the key `3a6b01bd-5f11-4cbe-87c4-c527895728a9`.

Now let's assume the software we have just created got was created with the ID `bc777bbe-dc44-4a8c-9b2b-23c7cfd5a007`. We will need this ID to create features.

#### Create a feature
```
java -jar manager.jar feature create --software "bc777bbe-dc44-4a8c-9b2b-23c7cfd5a007" --id "MY-UNIQUE-FEATURE-ID" --name "MyFeature" --default "true"
```
We have now created a feature with the ID `MY-UNIQUE-FEATURE-ID`. The name is just for us, so we can identify this feature in the manager. The value is set to "true" per default. So our software needs to evaluate that value to see if the feature is enabled.

#### Issuing a license
_System ID is only applicable for license version 2. Version 1 does not need a system ID_  

To get the ID for your system, simply
- run the IDGenerator
- choose `MAC` from the `selector` on the left
- copy the key from the text box on the right

This is your `binding` for this license.

Assuming we have received the ID `qwertyuiop` from the system where the software will be installed we are now able to issue the license. However, we want to disable our new feature for this client.

```
java -jar manager.jar license create --software bc777bbe-dc44-4a8c-9b2b-23c7cfd5a007 --feature my-unique-feature-id=false --binding qwertyuiop --name "my first customer"
```
Here we go. Our license has been issued (let's assume with ID `093889a8-a87f-4619-83a4-7d99f380b682`) to `my first customer` on software `bc777bbe-dc44-4a8c-9b2b-23c7cfd5a007`.
Feature `my-unique-feature-id` has been set to `false` in this license. The license has been bound to the system with the ID `qwertyuiop`.
    
#### Exporting the license  
Now, we just have to export our license to a file we can send to the customer. For that we simply use
```
java -jar manager.jar license export --license 093889a8-a87f-4619-83a4-7d99f380b682 --file "./licenses/my_first_customer.lic"
```
License `093889a8-a87f-4619-83a4-7d99f380b682` is being exported to `./licenses/my_first_customer.lic`. 

#### Automation
Of course we have automation in mind. You can use the `-j` or `--json` switch in the CLI to receive a JSON result and process it to your liking.
```
java -jar manager.jar license export --license 093889a8-a87f-4619-83a4-7d99f380b682 --file "./licenses/my_first_customer.lic" --json
```
Will produce a json output with everything you need to know. Including your license in Base64-Format.
```
{
  "exports": [
    {
      "base64License": "W6Aj4AAAAgBYv0/mpCGBo4aHaXWs1PTZd4cwTcpRmSgHZpcIg6UbQxcho1UIzha+ENXdbSyAhA4A1NtFk6H37U/3boJwe6ezKHYDzzZ0CPzQ7DL3xjXJY6mMjbNeNuMwzJapqoJ9b308yeYDzVA45+rxUkCPf5b6/jKTwJ9fvcY6r2IEDJZ2OGridKsBLAKaI33J4CQe5O6XO1pNvGk/fsPK06J/+tAZIKBK83EDSx+1HwhBzfHuoPhmDZLaVx6oNQl3Ug7kt3sNowmV/RA4r53sQX4maKNKHA2yZbSRri4q09lvaHxDnupLerUWlczqBPqSjqPyeITuJkAcqj2Zy4JtgAWGJa4YV3eIK29HAZGP9876oJl2ho4kRnoM9pSnKLCl4F8Zun8wn4jPGpeXHj7RP4wjSlYuRgKHnA341LIKnPYel+oWv02Z2ZVrb5fwe91vTAaDM3FgMsFZVV4cafR2omRKKx+z5Fu+bYo9LwVOpF43dknmIGPA587O0yvVzb1qgQjJH3QJTe6SSBjGJ6k3xDVqwLirLXud0/4iqWnee75n0mEqa+AoVX+6UKxBFs9eFGwS4h/RfZnN/in4ANNh85Kk9hFpHZMyGQc8EiLwBCxCuM765CmZClNJIqIS0ftB7/UVjb/j8I/1JGwyXXg0NVcCw8Lb4PUnndzlHPHY+wi5lAlc6wAAADAm6iWdyozpRW2lbwgGhqlFMnMzm4ysZX6d+zRCPNyhKfTO153Tof7D8mmiKazyINUAAAIAQHGkEeFTHxuKusjvhYVg/87Mkes0eVeBZiVJC6C5+wQKrbrHe2nHpE5qrRowYmItkAyiis/7kh6yZHp/ZV1ti78w73WfGRZ2nWqSPykH+cjU+qTPwFMN41s+3RGtGy/wQjtvKxithcSLJHk9qX6uHreCJPcnfbjQw35DHSkb6ZtklRieRSlvwNbWu+JdwmaRjf0qDBVQZ93te46zGLzxQ6SubjyUKB7ToyNlZZMPkUU2zs9QIRQtgTruQDhwvP+iVlZvtnvrHCyzZz6/7ApNFuQoHKqtcShVaJ21hlzTQ7Y6Ad0haXHqnIYtOk3cKX3zmNeXkngmqHx0hq3n8SJVy7LsuCh0ki8taLJQOUzQ9o1syn1ArEcM7bwCXWBa8eOXm+V1UH9L2MP3hWpOrocZzTjp0t0g1DCYUBsD5r1EQe/Z0320ZkCX3LFxKTHizYDRDBHGUK5R/cgWwnn5cbcCcgu0hs0VooU3sXu0GBg5JvU5dQfYvDMJjQX5SoHC+K7cKyb6+pGJDAOWFsqhPb6vAh6S9Mn3+s40sPL71+YTo+OxfPb/UacXoTicohzOVmIdardgBXpwZHJLfMKZ1DiBasrRYuXC0g5omXFpC4LBR7nFp6FURweQJfsKJ+0HZ3Q+lVa3DWYrIQwV80bSYj1hjhabo21CNnZ7WojeLxOIsNA\u003d",
      "filePath": "D:\\showcase\\greenLicense\\manager\\.\\licenses\\my_first_customer.lic",
      "license": {
        "id": "093889a8-a87f-4619-83a4-7d99f380b682",
        "licenseId": "qwertyuiop",
        "name": "my first customer",
        "software": {
          "features": [
            {
              "id": "my-unique-feature-id",
              "name": "MyFeature",
              "value": "false"
            }
          ],
          "id": "bc777bbe-dc44-4a8c-9b2b-23c7cfd5a007",
          "keyPairId": "3a6b01bd-5f11-4cbe-87c4-c527895728a9",
          "licenseVersion": "LICENSE_V2",
          "name": "MyCoolSoftware",
          "version": "1.0"
        }
      }
    }
  ],
  "errorMessages": [],
  "success": true
}
```

## The reader
How to use the license reader in my software?
First you have to include the `common`-module in your project.

Most important for you is the interface `GreenLicenseValidator`. This interface takes either `GreenLicenseReaderV1` or `GreenLicenseReaderV2`. Where the first reads V1-Licenses and the latter V2-Licenses (the ones with the system ID).

### Example - reading license from file
Get the public key as bytes with the following command:
```
java -jar manager.jar keypair show --id 3a6b01bd-5f11-4cbe-87c4-c527895728a9 -b
```

Take the byte output and hard code it into your program just like in the example class below:

```
import de.shadowsoft.greenLicense.common.license.GreenLicense;
import de.shadowsoft.greenLicense.common.license.GreenLicenseReaderV2;
import de.shadowsoft.greenLicense.common.license.GreenLicenseValidator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class Example {
    private static byte pk[] = new byte[]{
            (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x22, (byte) 0x30, (byte) 0x0D, (byte) 0x06, (byte) 0x09, (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x82, (byte) 0x02, (byte) 0x0F, (byte) 0x00, (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x0A, (byte) 0x02, (byte) 0x82,
            (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x97, (byte) 0x57, (byte) 0x7A, (byte) 0x51, (byte) 0xD8, (byte) 0x4A, (byte) 0x5A, (byte) 0x99, (byte) 0xEB, (byte) 0xCB, (byte) 0xEE, (byte) 0xB3,
            (byte) 0x9C, (byte) 0xA8, (byte) 0x1D, (byte) 0xB9, (byte) 0x77, (byte) 0xB7, (byte) 0xC7, (byte) 0xBC, (byte) 0x01, (byte) 0x95, (byte) 0xFF, (byte) 0x1F, (byte) 0x3E, (byte) 0x9C, (byte) 0xE3,
            (byte) 0xD3, (byte) 0x9E, (byte) 0x88, (byte) 0x9D, (byte) 0xC4, (byte) 0xDA, (byte) 0x22, (byte) 0xCD, (byte) 0xD7, (byte) 0xF8, (byte) 0x09, (byte) 0xE5, (byte) 0x5A, (byte) 0x90, (byte) 0xFE,
            (byte) 0x3F, (byte) 0x36, (byte) 0x8B, (byte) 0x6A, (byte) 0x87, (byte) 0x2E, (byte) 0x52, (byte) 0xCA, (byte) 0x3A, (byte) 0x48, (byte) 0xB7, (byte) 0xC8, (byte) 0xA8, (byte) 0x3D, (byte) 0x5B,
            (byte) 0x93, (byte) 0x46, (byte) 0xFE, (byte) 0xC3, (byte) 0xCE, (byte) 0x52, (byte) 0x37, (byte) 0xC4, (byte) 0x9B, (byte) 0x84, (byte) 0xCE, (byte) 0xDA, (byte) 0x06, (byte) 0xCA, (byte) 0x17,
            (byte) 0x6C, (byte) 0xC7, (byte) 0xBA, (byte) 0x1C, (byte) 0x77, (byte) 0xD5, (byte) 0xCE, (byte) 0xC4, (byte) 0x38, (byte) 0x3B, (byte) 0x3F, (byte) 0x24, (byte) 0x47, (byte) 0xAA, (byte) 0x02,
            (byte) 0x08, (byte) 0x0C, (byte) 0x0D, (byte) 0xA7, (byte) 0xD4, (byte) 0x52, (byte) 0xFB, (byte) 0xE5, (byte) 0x39, (byte) 0x07, (byte) 0x9B, (byte) 0xB7, (byte) 0xE3, (byte) 0xFA, (byte) 0xB9,
            (byte) 0xB2, (byte) 0x1B, (byte) 0xC4, (byte) 0x91, (byte) 0x75, (byte) 0xE7, (byte) 0x4B, (byte) 0x90, (byte) 0xC8, (byte) 0x9A, (byte) 0x90, (byte) 0x06, (byte) 0x15, (byte) 0xF9, (byte) 0x21,
            (byte) 0xD8, (byte) 0x66, (byte) 0xDF, (byte) 0x18, (byte) 0xC2, (byte) 0xF1, (byte) 0xCF, (byte) 0xD0, (byte) 0x74, (byte) 0x2A, (byte) 0x59, (byte) 0x9D, (byte) 0x15, (byte) 0xE5, (byte) 0x4D,
            (byte) 0x53, (byte) 0xB2, (byte) 0xC6, (byte) 0x3B, (byte) 0xD7, (byte) 0x56, (byte) 0x75, (byte) 0xCD, (byte) 0x79, (byte) 0x36, (byte) 0x54, (byte) 0x44, (byte) 0x7A, (byte) 0xEA, (byte) 0x96,
            (byte) 0x31, (byte) 0x84, (byte) 0xEB, (byte) 0xA3, (byte) 0xCF, (byte) 0x5A, (byte) 0x16, (byte) 0x91, (byte) 0xDB, (byte) 0xE7, (byte) 0x70, (byte) 0xC7, (byte) 0x7E, (byte) 0xD4, (byte) 0x50,
            (byte) 0xA2, (byte) 0x47, (byte) 0xC9, (byte) 0xC1, (byte) 0xFF, (byte) 0x7E, (byte) 0x6C, (byte) 0x76, (byte) 0x1A, (byte) 0x02, (byte) 0x53, (byte) 0xB2, (byte) 0x9B, (byte) 0xD3, (byte) 0xD5,
            (byte) 0x8F, (byte) 0x50, (byte) 0xC1, (byte) 0x0C, (byte) 0x1C, (byte) 0x72, (byte) 0xA7, (byte) 0xE6, (byte) 0x71, (byte) 0x02, (byte) 0xBF, (byte) 0x8D, (byte) 0x01, (byte) 0x45, (byte) 0xA3,
            (byte) 0xA5, (byte) 0xA6, (byte) 0x97, (byte) 0x53, (byte) 0xA9, (byte) 0x98, (byte) 0x31, (byte) 0x22, (byte) 0xED, (byte) 0xB6, (byte) 0xF4, (byte) 0xA9, (byte) 0xAC, (byte) 0x41, (byte) 0xD3,
            (byte) 0x74, (byte) 0x95, (byte) 0x05, (byte) 0xB5, (byte) 0xBF, (byte) 0x2D, (byte) 0x3B, (byte) 0x1C, (byte) 0xF9, (byte) 0x15, (byte) 0xA7, (byte) 0x14, (byte) 0x49, (byte) 0x9D, (byte) 0x5E,
            (byte) 0x1E, (byte) 0x78, (byte) 0xE6, (byte) 0x1A, (byte) 0x9A, (byte) 0xDD, (byte) 0xCB, (byte) 0x8F, (byte) 0x1D, (byte) 0xD2, (byte) 0xB7, (byte) 0x7D, (byte) 0xBB, (byte) 0x8A, (byte) 0xC1,
            (byte) 0xBA, (byte) 0xCE, (byte) 0xDF, (byte) 0x30, (byte) 0x34, (byte) 0xA0, (byte) 0xE3, (byte) 0x37, (byte) 0x3D, (byte) 0x0F, (byte) 0xA2, (byte) 0x56, (byte) 0xB2, (byte) 0x38, (byte) 0xD1,
            (byte) 0xCF, (byte) 0xF9, (byte) 0x2B, (byte) 0x03, (byte) 0x1F, (byte) 0xD1, (byte) 0xA4, (byte) 0xD1, (byte) 0x83, (byte) 0x1E, (byte) 0x70, (byte) 0x6C, (byte) 0x8F, (byte) 0xD3, (byte) 0x86,
            (byte) 0xC9, (byte) 0x58, (byte) 0xE9, (byte) 0x9B, (byte) 0xC2, (byte) 0x33, (byte) 0xBF, (byte) 0x98, (byte) 0x83, (byte) 0x9B, (byte) 0xAD, (byte) 0xCB, (byte) 0x41, (byte) 0xFC, (byte) 0xE0,
            (byte) 0x30, (byte) 0xD5, (byte) 0xBA, (byte) 0x0A, (byte) 0xEE, (byte) 0xDE, (byte) 0x95, (byte) 0x0D, (byte) 0xB9, (byte) 0x7D, (byte) 0x13, (byte) 0x51, (byte) 0xB5, (byte) 0x89, (byte) 0xF1,
            (byte) 0xAE, (byte) 0x18, (byte) 0x1E, (byte) 0xF3, (byte) 0x5C, (byte) 0xF2, (byte) 0x59, (byte) 0x09, (byte) 0xF3, (byte) 0x80, (byte) 0x0B, (byte) 0xAA, (byte) 0x8E, (byte) 0xCC, (byte) 0xF9,
            (byte) 0x2E, (byte) 0x6F, (byte) 0xCE, (byte) 0xA9, (byte) 0x75, (byte) 0xF7, (byte) 0x77, (byte) 0x4D, (byte) 0x8B, (byte) 0xA6, (byte) 0xFD, (byte) 0x80, (byte) 0x3E, (byte) 0x07, (byte) 0xDF,
            (byte) 0x4E, (byte) 0xAB, (byte) 0x2D, (byte) 0x89, (byte) 0x9F, (byte) 0x4E, (byte) 0x17, (byte) 0xEF, (byte) 0x8B, (byte) 0xF9, (byte) 0x96, (byte) 0xBF, (byte) 0x0D, (byte) 0x97, (byte) 0x96,
            (byte) 0x10, (byte) 0x26, (byte) 0xF8, (byte) 0x93, (byte) 0x73, (byte) 0xB6, (byte) 0x58, (byte) 0x23, (byte) 0x9A, (byte) 0x49, (byte) 0x6D, (byte) 0x7A, (byte) 0x94, (byte) 0xB4, (byte) 0x92,
            (byte) 0xC7, (byte) 0x49, (byte) 0x2F, (byte) 0x57, (byte) 0x59, (byte) 0x20, (byte) 0xED, (byte) 0xF1, (byte) 0x9E, (byte) 0x80, (byte) 0x4B, (byte) 0x22, (byte) 0xCE, (byte) 0xE0, (byte) 0x38,
            (byte) 0x55, (byte) 0x83, (byte) 0x72, (byte) 0x5A, (byte) 0x7B, (byte) 0xF0, (byte) 0x53, (byte) 0xBF, (byte) 0x7B, (byte) 0xCF, (byte) 0x7D, (byte) 0x8C, (byte) 0x6C, (byte) 0x45, (byte) 0x99,
            (byte) 0xE2, (byte) 0x35, (byte) 0xEC, (byte) 0xED, (byte) 0xAF, (byte) 0x48, (byte) 0x87, (byte) 0x10, (byte) 0x0A, (byte) 0xB1, (byte) 0xD9, (byte) 0xC4, (byte) 0xE1, (byte) 0x1E, (byte) 0x6A,
            (byte) 0xF0, (byte) 0x7B, (byte) 0x7D, (byte) 0x41, (byte) 0x8E, (byte) 0x99, (byte) 0x39, (byte) 0x36, (byte) 0x0A, (byte) 0x04, (byte) 0x98, (byte) 0x7E, (byte) 0xF8, (byte) 0xE3, (byte) 0x18,
            (byte) 0xF2, (byte) 0x08, (byte) 0x85, (byte) 0x6C, (byte) 0x26, (byte) 0xED, (byte) 0xB1, (byte) 0xCD, (byte) 0x42, (byte) 0xA8, (byte) 0x81, (byte) 0x0D, (byte) 0x91, (byte) 0x37, (byte) 0xEF,
            (byte) 0x10, (byte) 0x07, (byte) 0x2A, (byte) 0x04, (byte) 0x1A, (byte) 0x7D, (byte) 0xD1, (byte) 0x43, (byte) 0x35, (byte) 0xD1, (byte) 0x77, (byte) 0xD4, (byte) 0x60, (byte) 0xA7, (byte) 0xCC,
            (byte) 0x9A, (byte) 0x8D, (byte) 0xEF, (byte) 0x80, (byte) 0x7A, (byte) 0x6F, (byte) 0x40, (byte) 0x8B, (byte) 0x14, (byte) 0x85, (byte) 0x3B, (byte) 0x6F, (byte) 0xCD, (byte) 0xFE, (byte) 0x61,
            (byte) 0x61, (byte) 0x60, (byte) 0xFD, (byte) 0x36, (byte) 0xC0, (byte) 0x80, (byte) 0x38, (byte) 0x92, (byte) 0x77, (byte) 0x62, (byte) 0x33, (byte) 0x58, (byte) 0xBD, (byte) 0x7D, (byte) 0x70,
            (byte) 0xB1, (byte) 0xED, (byte) 0x52, (byte) 0x9F, (byte) 0xDE, (byte) 0xA2, (byte) 0x0A, (byte) 0xD2, (byte) 0xA0, (byte) 0xB7, (byte) 0xE9, (byte) 0xE3, (byte) 0xB9, (byte) 0xAC, (byte) 0xDA,
            (byte) 0x84, (byte) 0x16, (byte) 0xAA, (byte) 0xAF, (byte) 0x1D, (byte) 0x4A, (byte) 0xB9, (byte) 0x4E, (byte) 0x7A, (byte) 0x3D, (byte) 0x2C, (byte) 0x8A, (byte) 0xDF, (byte) 0x16, (byte) 0xB0,
            (byte) 0xEB, (byte) 0x26, (byte) 0x2E, (byte) 0x3C, (byte) 0x9D, (byte) 0x02, (byte) 0x03, (byte) 0x01, (byte) 0x00, (byte) 0x01
    };

    public static void main(String[] args) {
        GreenLicenseValidator validator = new GreenLicenseReaderV2(pk);
        try {
            GreenLicense license = validator.readLicenseFromFile("D:\\showcase\\greenLicense\\manager\\license\\test.lic");
            if (license.isValid()) {
                System.out.println("License OK!");
                for (Map.Entry<String, String> feature : license.getFeature().entrySet()) {
                    System.out.println(feature.getKey() + "=" + feature.getValue());
                }
            } else {
                System.err.println("License invalid!");
            }
        } catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | SignatureException | InvalidKeySpecException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
```

`license.isValid()` checks if the license is valid.
 - correct header
 - correct signature
 - correct system (for license V2 only)


The features can be retrieved as a String map where the `key` is the feature ID
and the `value` is the feature value.

# Known issues
- Currently, there seems to be an error when reading the Base64 codes license.