# TcgaGSData

This is for educational and research purposes only. 

http://bioinformatics.mdanderson.org

The goal of the Gene Survey project is to take TCGA Standardized Data and make gene level cross-disease and sometimes cross platform comparisons and evaluations simpler to perform, as these represent common questions asked of PIs and analysts. That is, a common question is: how does gene XYZ act across disease types?

This is one of the two Java applications used for this project. If you are developer or researcher recompiling or modifying this software, start with the TcgaIdConverter project. TcgaGSData depends on the TcgaIdConverter project.

The TcgaGSData application performs two actions. First, the TcgaGSData application takes data downloaded by the TcgaIdConverter application and from the TCGA Standardized Data and converts that data into formats which allow quicker access for the Gene Survey project. (This access generally means accessing data for all disease types based on a gene symbol.) Second, the TcgaGSData provides a Java link between the data and the R package most users use for statistical analysis.

TcgaGSData is a Netbeans project. When the project is first opened in Netbeans, a warning about unresolved references will popup. This warning is because TcgaGSData uses the JAR and libraries from TcgaIdConverter. The user should use the "resolve" option and locate the package JAR and libraries from the other project.

Once compiled, all six JAR files go into the inst/TcgaGSData directory in the TCGAGeneReport packge. (That package already has copies of these JARs.)