package app.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="task_id")
    private Long   id;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    private String content;
    @Column(name="creation_date")
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date   creationDate;
    @Column(name="end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date   endDate;

    protected Task() {}

    public Task(Category category, String content ) {
        this.category = category;
        this.content  = content;
        //this.creationDate     = new Date();
    }

    @Override
    public String toString() {
        return String.format(
                "Task[id=%d, content='%s']", id, content);
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
