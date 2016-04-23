// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.stardroid.units;

import java.util.Date;


import com.google.android.stardroid.util.Geometry;
import com.google.android.stardroid.util.MathUtil;

public class HeliocentricCoordinates extends Vector3 {
  public float radius;  // Radius. (AU)

  // Value of the obliquity of the ecliptic for J2000
  private static final float OBLIQUITY = 23.439281f * Geometry.DEGREES_TO_RADIANS;

  public HeliocentricCoordinates(float radius, float xh, float yh, float zh) {
    super(xh, yh, zh);
    this.radius = radius;
  }

  /**
   * Subtracts the values of the given heliocentric coordinates from this
   * object.
   */
  public void Subtract(HeliocentricCoordinates other) {
    this.x -= other.x;
    this.y -= other.y;
    this.z -= other.z;
  }

  public HeliocentricCoordinates CalculateEquatorialCoordinates() {
    return new HeliocentricCoordinates(this.radius,
        this.x,
        this.y * MathUtil.cos(OBLIQUITY) - this.z * MathUtil.sin(OBLIQUITY),
        this.y * MathUtil.sin(OBLIQUITY) + this.z * MathUtil.cos(OBLIQUITY));
  }

  public float DistanceFrom(HeliocentricCoordinates other) {
    float dx = this.x - other.x;
    float dy = this.y - other.y;
    float dz = this.z - other.z;
    return MathUtil.sqrt(dx * dx + dy * dy + dz * dz);
  }



  @Override public String toString() {
    return String.format("(%f, %f, %f, %f)", x, y, z, radius);
  }
}
