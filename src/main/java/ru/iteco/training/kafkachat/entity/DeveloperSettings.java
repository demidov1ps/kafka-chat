package ru.iteco.training.kafkachat.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dev_settings")
public class DeveloperSettings extends Settings {
}
