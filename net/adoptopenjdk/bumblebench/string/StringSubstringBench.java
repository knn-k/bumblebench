/*******************************************************************************
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/

package net.adoptopenjdk.bumblebench.string;

import net.adoptopenjdk.bumblebench.core.MicroBench;

import java.util.Random;

/**
 *
 * String.substring() can be accelerated by vector instructions.
 *
 * This benchmark invokes substring() on a String to measure performance.
 *
 * The reported score is in terms of number of string substrings per sec.
 *
 **/

public final class StringSubstringBench extends MicroBench {

	private static final int MAX_ITERATIONS_PER_LOOP = option("maxIterations", 10000000);

	private static final int arraySize = 1024;
	private static String s;

	private static int value = 0;

	static {
		Random rand = new Random();

		char[] chars = new char[arraySize];
		for (int i = 0; i < arraySize-1; i++) {
			chars[i] = StringTestData.POSSIBLE_CHARS[rand.nextInt(StringTestData.POSSIBLE_CHARS.length)];
		}
		chars[arraySize-1] = 256; // U+0100, to make a String in the UTF16 mode
		s = new String(chars);
	}

	protected int maxIterationsPerLoop() {
		return MAX_ITERATIONS_PER_LOOP;
	}

	protected long doBatch(long numIterations) throws InterruptedException {
		pauseTimer();
		for (long loop = 0; loop < numIterations; loop++) {
			int len = ((int)loop % (arraySize - 1)) + 1; // 1 or larger
			startTimer();
			String x = s.substring(0, len); // The result String is in the LATIN1 mode
			pauseTimer();
			value += x.length();
		}
		return numIterations;
	}
}
