package com.goropai.resourceservice.service;

import com.goropai.resourceservice.entity.dto.Mp3FileDto;
import com.goropai.resourceservice.entity.dto.Mp3MetadataDto;
import com.goropai.resourceservice.entity.dto.SongIdsResponse;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataService {
    private final SongServiceProxy songServiceProxy;

    public MetadataService(SongServiceProxy songServiceProxy) {
        this.songServiceProxy = songServiceProxy;
    }

    @Transactional
    public Mp3MetadataDto parseAndSave(Mp3FileDto mp3FileDto) throws IOException {
        try (ByteArrayInputStream input = new ByteArrayInputStream(mp3FileDto.getData())) {
            Tika tika = new Tika();
            Metadata metadata = new Metadata();
            tika.parse(input, metadata);
            return songServiceProxy.save(new Mp3MetadataDto(mp3FileDto.getId(),
                            metadata.get("dc:creator"),
                            metadata.get("dc:title"),
                            metadata.get("xmpDM:album"),
                            metadata.get("xmpDM:releaseDate"),
                            getDurationFormatted(Double.parseDouble(metadata.get("xmpDM:duration"))))).getBody();
        }
    }

    public static String getDurationFormatted(double duration) {
        Duration d = Duration.ofSeconds((long) duration);
        return String.format("%02d:%02d", d.toMinutesPart(), d.toSecondsPart());
    }

    @Transactional
    public SongIdsResponse deleteMetadata(List<Integer> ids) {
        if (ids.isEmpty()) {
            return new SongIdsResponse();
        }
        return songServiceProxy.deleteById(convertListToString(ids)).getBody();
    }

    private String convertListToString(List<Integer> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
