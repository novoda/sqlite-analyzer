package tv.arte.plus7.sql
import com.novoda.sqlite.*
import com.novoda.sqlite.generator.ColumnsGenerator
import com.novoda.sqlite.generator.DBPrinter
import com.novoda.sqlite.generator.TableGenerator
import com.novoda.sqlite.generator.TablesGenerator
import com.novoda.sqlite.model.Database
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
/**
 * This task runs the SqliteAnalyzer to generate code to describe the database tables and columns.
 *
 * We use input and output directory annotations to ensure the task is run on any changes in the migrations directory
 * or when the output is removed.
 */
class GenerateDatabaseInfo extends DefaultTask {

    @InputDirectory
    File migrationsDir

    @OutputDirectory
    File outputDir

    String packageName = "com.novoda.database"

    @TaskAction
    void generate() {
        Database database = analyzeDb()
        generateCode(database)
    }

    private Database analyzeDb() {
        def arteMigrations = new MigrationsInDir(migrationsDir)
        def connection = new Migrator(arteMigrations).runMigrations()
        def database = new Analyzer(connection).analyze()
        connection.close()
        database
    }

    private void generateCode(Database database) {
        def dBPrinter = new DBPrinter()
        dBPrinter.packageName = packageName
        dBPrinter.targetDir = makeFileDir().absolutePath
        dBPrinter.printers = [new ColumnsGenerator(database), new TablesGenerator(database)]
        database.getTables().each { table ->
            dBPrinter.printers << new TableGenerator(table)
        }
        dBPrinter.print()
/*
        Printer[] printers = [new ColumnsPrinter(database), new TablesPrinter(database)]
        def codeGenerator = new Generator(makeFileDir(), packageName, printers)
        codeGenerator.print()
*/
    }

    private File makeFileDir() {
        String packageAsDir = packageName.replaceAll(~/\./, "/")
        def fileDir = new File(outputDir, packageAsDir)
        fileDir.mkdirs()
        return fileDir
    }
}

