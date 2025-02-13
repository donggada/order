package com.teamFresh.order.factoty.parser;

import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileParserFactory {
    private final Map<Class, FileParser> fileParserMap;

    public FileParserFactory(List<FileParser> fileParsers) {
        this.fileParserMap = fileParsers.stream()
                .collect(
                        Collectors.toMap(
                                AopUtils::getTargetClass,
                                service -> service
                        )
                );
    }

    public FileParser getService(FileParserType type) {
        if (Objects.requireNonNull(type) == FileParserType.EXCEL) {
            return fileParserMap.get(ExcelParser.class);
        }
        throw new IllegalArgumentException("Unsupported mall type: " + type);
    }

}
