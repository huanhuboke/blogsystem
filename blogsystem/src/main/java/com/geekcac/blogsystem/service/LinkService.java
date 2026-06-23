package com.geekcac.blogsystem.service;

import com.geekcac.blogsystem.domain.Link;
import com.geekcac.blogsystem.mapper.LinkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkMapper linkMapper;

    public Map<Integer, List<Link>> getLinks() {
        List<Link> links = this.linkMapper.findLinkList();
        if (!CollectionUtils.isEmpty(links)) {
            Map<Integer, List<Link>> linksMap = links.stream().collect(Collectors.groupingBy(Link::getLinkType));
            return linksMap;
        }
        return null;
    }
}
