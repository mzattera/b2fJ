using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ClassToC
{
    class Program
    {
        static void Main(string[] args)
        {
            // Determines path for sources
            String sourceFile = null;
            if (args.Length > 0)
            {
                sourceFile = args[0];
            }
            else {
                Console.WriteLine("Provide name of class file to convert; this must be a NsTVM linked binary.");
                Environment.Exit(-1);
            }
            String sourcePath = Path.GetDirectoryName(sourceFile);

            byte[] fileBytes = File.ReadAllBytes(sourceFile);

            StringBuilder sb = new StringBuilder();
            sb.AppendLine("/*");
            sb.Append(" * Source binary: ").AppendLine(sourceFile);
            sb.AppendLine(" *");
            sb.AppendLine(" * This is a machine-generated file; do not modify.");
            sb.AppendLine(" * This contains the Java code to be executed as an array of bytes,");
            sb.AppendLine(" * so it can be linked directly in the JVM code, instead of loading it.");
            sb.AppendLine(" */");
            sb.AppendLine();
            sb.AppendLine("#ifndef _JAVA_CODE_H_");
            sb.AppendLine("#define _JAVA_CODE_H_");
            sb.AppendLine();
            sb.AppendLine("#include \"platform_config.h\"");
            sb.AppendLine();
            sb.AppendLine("byte javaClassFileContent[] = {").Append("\t");
            int c = 0;
            foreach (byte b in fileBytes)
            {
                if (c > 0)
                {
                    sb.Append(", ");
                    if ((c % 16) == 0) sb.AppendLine().Append("\t");
                }
                sb.Append("0x").Append(Convert.ToString(b, 16).PadLeft(2, '0'));
                ++c;
            }
            sb.AppendLine();
            sb.AppendLine("};");
            sb.AppendLine("#endif");

            File.WriteAllText(Path.Combine(sourcePath, "java_code.h"), sb.ToString());

            Console.WriteLine("\nFile created successfully: java_code.h");
            Environment.Exit(0);
        }
    }
}