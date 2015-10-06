/* Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.camunda.bpm.engine.impl.core.variable.value;

import org.camunda.commons.utils.EnsureUtil;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadablePeriod;

/**
 * Wrapper of {@link Period} that implements {@link Comparable}. Note that the comparison
 * use normalized periods, assuming a 12 month year, 31 day month, 7 day week,
 * 24 hour day, 60 minute hour and 60 second minute.
 *
 * @author Philipp Ossler
 */
public class CompareablePeriod implements ReadablePeriod, Comparable<Object> {

  protected static final DateTime END_INSTANCE = new DateTime(1970, 1, 1, 0, 0);

  protected final Period period;

  public CompareablePeriod(Period period) {
    EnsureUtil.ensureNotNull("period", period);

    this.period = period;
  }

  @Override
  public int compareTo(Object obj) {
    if (obj instanceof Period) {
      return compareToPeriod((Period) obj);

    } else if (obj instanceof ReadablePeriod) {
      ReadablePeriod otherPeriod = (ReadablePeriod) obj;
      return compareToPeriod(otherPeriod.toPeriod());

    } else if (obj instanceof ReadableDuration) {
      return compareToDuration((ReadableDuration) obj);

    } else {
      throw new IllegalArgumentException("can not compare to object of class " + obj.getClass());
    }
  }

  public int compareToPeriod(Period otherPeriod) {
    return compareTo(otherPeriod.toDurationTo(END_INSTANCE));
  }

  public int compareToDuration(ReadableDuration otherDuration) {
    return period.toDurationTo(END_INSTANCE).compareTo(otherDuration);
  }

  @Override
  public PeriodType getPeriodType() {
    return period.getPeriodType();
  }

  @Override
  public int size() {
    return period.size();
  }

  @Override
  public DurationFieldType getFieldType(int index) {
    return period.getFieldType(index);
  }

  @Override
  public int getValue(int index) {
    return period.getValue(index);
  }

  @Override
  public int get(DurationFieldType field) {
    return period.get(field);
  }

  @Override
  public boolean isSupported(DurationFieldType field) {
    return period.isSupported(field);
  }

  @Override
  public Period toPeriod() {
    return period.toPeriod();
  }

  @Override
  public MutablePeriod toMutablePeriod() {
    return period.toMutablePeriod();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((period == null) ? 0 : period.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (obj instanceof Period) {
      return compareToPeriod((Period) obj) == 0;
    } else if (obj instanceof ReadablePeriod) {
      ReadablePeriod other = (ReadablePeriod) obj;
      return compareToPeriod(other.toPeriod()) == 0;
    } else if (obj instanceof ReadableDuration) {
      return compareToDuration((ReadableDuration) obj) == 0;

    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return "CompareablePeriod [period=" + period + "]";
  }

}
