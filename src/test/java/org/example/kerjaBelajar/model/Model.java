package org.example.kerjaBelajar.model;

/**
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "menu_special_category", schema = SUPPORTING_DATA)
@Where(clause = "row_status = 1")
@SQLDelete(sql = "UPDATE " + SUPPORTING_DATA + ".menu_special_category SET row_status = 0 WHERE id = ?", check = ResultCheckStyle.COUNT)
public class Model {
}
 */
