package com.novoda.sqlite;

import java.io.File;

public interface Migrations {

	Iterable<File> asIterable();

}
