# CAP-PRE

This repository contains the implementation and experimental evaluation for the paper **"Privacy-Preserving and Autonomous-Path Access Delegation for Mobile Cloud Healthcare Systems"**, proposing the CAP-PRE scheme for secure access delegation in mobile cloud healthcare systems. 

Implemented based on Java Pairing-Based Cryptography Library (JPBC)[1].

---



## 📦Implemented Schemes

This project implements and compares the following proxy re-encryption (PRE) schemes:

1. **CAP-PRE**: Our proposed scheme for autonomous-path access delegation.
2. **AP-PRE**: Autonomous Path Proxy Re-Encryption [2].
3. **APB-PRE**: A generalized autonomous path proxy re-encryption scheme for supporting branch functionality [3].
4. **IBET**: Identity-Based Encryption Transformation [4].
5. **FABRIC**: Fast and Secure Unbounded Cross-System Encrypted Data Sharing in Cloud Computing [5].

---



## 🛠️ Implementation Details

#### Dependencies
- **JDK 1.8+**: Required for Java compilation and execution.  
- **Maven**: Used for dependency management.  
- **JPBC Library**: Cryptographic operations are implemented using the JPBC Library [1].  

#### Setup Instructions
1. Install **JDK 1.8** and set the `JAVA_HOME` environment variable.  

2. Install **Maven** and ensure it is added to your system path.  

3. To use JPBC, include the following jars:

   ​	**jpbc-2.0.0-api.jar**

   ​	**jpbc-2.0.0-plaf.jar**

   ​	**jpbc-2.0.0-crypto.jar**

    or add the JPBC dependency to your `pom.xml`:

   ```xml
       <!-- https://mvnrepository.com/artifact/com.github.emilianobonassi.jpbc/jpbc-plaf -->
       <dependency>
         <groupId>com.github.emilianobonassi.jpbc</groupId>
         <artifactId>jpbc-plaf</artifactId>
         <version>2.0.0</version>
       </dependency>
       <!-- https://mvnrepository.com/artifact/com.github.emilianobonassi.jpbc/jpbc-crypto -->
       <dependency>
         <groupId>com.github.emilianobonassi.jpbc</groupId>
         <artifactId>jpbc-crypto</artifactId>
         <version>2.0.0</version>
       </dependency>
       <!-- https://mvnrepository.com/artifact/com.github.emilianobonassi.jpbc/jpbc-api -->
       <dependency>
         <groupId>com.github.emilianobonassi.jpbc</groupId>
         <artifactId>jpbc-api</artifactId>
         <version>2.0.0</version>
       </dependency>

---



## 🗂️ Code Structure

#### Core Implementation
All cryptographic schemes are implemented under **` src/main/java/bupt/`**:  
- **`bupt/CAPPRE`**: Our proposed CAP-PRE scheme .  
- **`bupt/APPRE`**: Autonomous Path Proxy Re-Encryption [2].
- **`bupt/APBPRE`**: A generalized autonomous path proxy re-encryption scheme for supporting branch functionality [3].
- **`bupt/IBET`**: Identity-Based Encryption Transformation [4].
- **`bupt/Fabric`**: Fast and Secure Unbounded Cross-System Encrypted Data Sharing in Cloud Computing [5].

Each scheme contains the following core modules:  
- `setup()`: Initializes system parameters.  
- `keyGen()`: Generates secret key.  
- `encrypt()`: Encrypts plaintext.  
- `rKGen()`: Generates re-encryption keys.  
- `reEncrypt()`: Transforms ciphertext.  
- `decrypt1()` & `decrypt2()`: Decryption for original and re-encrypted ciphertexts.  

#### Utility Classes  

- **`src/main/java/basic`**: Contains reusable components:  

#### Testing & Evaluation  

- **`src/main/java/test`**: Performance evaluation scripts:  
  - **`Main.java`**: Entry point to run all experiments. Execute via:  
  - **`testForN.java`**: Measures performance trends with varying receiver counts (*n*).  
  - **`testForL.java`**: Measures performance trends with varying delegation path length (*l*).  

---




## 📄Reference

[1] “JPBC Library,” http://gas.dia.unisa.it/projects/jpbc/

[2] Z. Cao, H. Wang, and Y. Zhao, “AP-PRE: Autonomous Path Proxy Re-Encryption and Its Applications,” IEEE Transactions on Dependable and Secure Computing, vol. 16, no. 5, pp. 833–842, Sep. 2019.

[3] Z. Lin, J. Zhou, Z. Cao, X. Dong, and K.-K. R. Choo, “Generalized Autonomous Path Proxy Re-Encryption Scheme to Support Branch Functionality,” IEEE Transactions on Information Forensics and Security, vol. 18, pp. 5387–5400, 2023.

[4] H. Deng, Z. Qin, Q. Wu, Z. Guan, and Y. Zhou, “Flexible attribute-based proxy re-encryption for efficient data sharing,” Information Sciences, vol.511, pp. 94–113, Feb. 2020.

[5] L. Wang, Y. Lin, T. Yao, H. Xiong, and K. Liang, “FABRIC: Fast and Secure Unbounded Cross-System Encrypted Data Sharing in Cloud Computing,” IEEE Transactions on Dependable and Secure Computing, vol. 20, no. 6, pp. 5130–5142, Nov. 2023.
