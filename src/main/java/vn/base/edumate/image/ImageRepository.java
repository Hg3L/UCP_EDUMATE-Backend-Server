package vn.base.edumate.image;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<List<Image>> findByPostId(Long postId);

    @Query("SELECT i FROM Image i WHERE i.post IS NULL AND i.comment IS NULL AND i.createdAt < :cutoff")
    List<Image> findImagesWithoutPostBefore(@Param("cutoff") Instant cutoff);
}
