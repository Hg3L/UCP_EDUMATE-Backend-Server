package vn.base.edumate.post_report;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostReportMapper {
    PostReportResponse toPostReportResponse(PostReport postReport);
}
