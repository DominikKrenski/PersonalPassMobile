package org.dominik.pass.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public final class OnboardItem {
  final int imageId;
  final String title;
  final String message;
}
